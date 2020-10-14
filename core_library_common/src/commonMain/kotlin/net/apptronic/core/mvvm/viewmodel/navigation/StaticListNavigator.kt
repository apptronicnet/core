package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.value
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter

fun <T, Id, VM : IViewModel> IViewModel.listBuilder(builder: ViewModelBuilder<T, Id, VM>): ViewModelListBuilder<T, Id, VM> {
    return viewModelListBuilder(builder)
}

fun <T, Id, VM : IViewModel> StaticListNavigator<*>.listBuilder(builder: ViewModelBuilder<T, Id, VM>): ViewModelListBuilder<T, Id, VM> {
    return navigatorContext.viewModelListBuilder(builder)
}

abstract class StaticListNavigator<State> internal constructor(
        parent: IViewModel,
        final override val navigatorContext: Context,
        state: State
) : BaseListNavigator<ListNavigatorContent<State>, State>(parent), VisibilityFilterableNavigator {

    private val visibilityFilters: VisibilityFilters<IViewModel> = VisibilityFilters<IViewModel>()
    private var listFilter: ListNavigatorFilter = simpleFilter()

    private var items: List<ViewModelContainer> = emptyList()
    private var visibleItems: List<ViewModelContainer> = emptyList()

    private val contentData = parent.value<ListNavigatorContent<State>>()

    override val content = contentData

    private fun sendStatusUpdate(state: State) {
        contentData.set(ListNavigatorContent(items.map { it.getViewModel() }, visibleItems.map { it.getViewModel() }, state))
    }

    init {
        sendStatusUpdate(state)
    }

    override fun getVisibilityFilters(): VisibilityFilters<IViewModel> {
        return visibilityFilters
    }

    fun setListFilter(listFilter: ListNavigatorFilter) {
        this.listFilter = listFilter
        refreshVisibility(contentData.get().state, null)
    }

    private fun refreshVisibility(state: State, changeInfo: Any?) {
        val source = items.map {
            ItemVisibilityRequest(it.getViewModel(), it.shouldShow())
        }
        val filterResult = listFilter.filterList(source)
        visibleItems = items.filter {
            filterResult.contains(it.getViewModel())
        }
        val filteredState = calculateFilteredState(items.map { it.getViewModel() }, filterResult, state)
        sendStatusUpdate(filteredState)
        notifyAdapter(changeInfo)
    }

    protected abstract fun calculateFilteredState(all: List<IViewModel>, visible: List<IViewModel>, state: State): State

    override fun onNotifyAdapter(adapter: ViewModelListAdapter<State>, changeInfo: Any?) {
        adapter.onDataChanged(visibleItems.map { ViewModelItem(it, this) }, contentData.get().state, changeInfo)
    }

    fun getAll(): List<IViewModel> {
        return items.map { it.getViewModel() }
    }

    fun getVisible(): List<IViewModel> {
        return visibleItems.map { it.getViewModel() }
    }

    private val containers = mutableMapOf<Long, ViewModelContainer>()

    init {
        parent.doOnTerminate {
            containers.values.forEach {
                it.terminate()
            }
        }
    }

    private fun IViewModel.getContainer(): ViewModelContainer? {
        return containers[componentId]
    }

    fun update(changeInfo: Any? = null, state: State, action: (MutableList<IViewModel>) -> Unit) {
        val list = items.map { it.getViewModel() }.toMutableList()
        action.invoke(list)
        set(list, state, changeInfo)
    }

    fun setState(state: State, changeInfo: Any? = null) {
        refreshVisibility(state, changeInfo)
    }

    fun set(value: List<IViewModel>, state: State, changeInfo: Any? = null) {
        val diff = getDiff(items.map { it.getViewModel() }, value)
        diff.removed.forEach {
            onRemoved(it)
        }
        val newVideModels = value.toTypedArray().toList()
        diff.added.forEach {
            it.verifyContext()
            onAdded(it)
        }
        items = newVideModels.mapNotNull { it.getContainer() }
        refreshVisibility(state, changeInfo)
    }

    private fun onAdded(viewModel: IViewModel) {
        val container = ViewModelContainer(viewModel, parent, visibilityFilters.isReadyToShow(viewModel))
        containers[viewModel.componentId] = container
        container.getViewModel().onAttachToParent(this)
        container.observeVisibilityChanged {
            if (it) {
                refreshVisibility(contentData.get().state, ItemAdded(indexOfViewModel(viewModel)))
            } else {
                refreshVisibility(contentData.get().state, ItemRemoved(indexOfViewModel(viewModel)))
            }
        }
        container.setAttached(true)
    }

    private fun onRemoved(viewModel: IViewModel) {
        viewModel.getContainer()?.let { container ->
            container.getViewModel().onDetachFromParent()
            container.terminate()
        }
    }

    override fun onSetAdapter(adapter: ViewModelListAdapter<State>) {
        parent.context.lifecycle.onExitFromActiveStage {
            items.forEach {
                if (boundIds.contains(it.getViewModel().componentId)) {
                    it.setFocused(false)
                    it.setVisible(false)
                    it.setBound(false)
                }
            }
            boundIds.clear()
        }
    }

    private val boundIds = mutableSetOf<Long>()

    override fun getSize(): Int {
        return visibleItems.size
    }

    override fun getState(): State {
        return contentData.get().state
    }

    override fun getViewModelItems(): List<ViewModelItem> {
        return visibleItems.map { ViewModelItem(it, this) }
    }

    override fun indexOfViewModel(viewModel: IViewModel): Int {
        return visibleItems.indexOfFirst { it.getViewModel() == viewModel }
    }

    override fun getViewModelItemAt(index: Int): ViewModelItem {
        return ViewModelItem(visibleItems[index], this)
    }

    override fun setBound(viewModel: IViewModel, isBound: Boolean) {
        viewModel.getContainer()?.setBound(isBound)
        if (isBound) {
            boundIds.add(viewModel.componentId)
        } else {
            boundIds.remove(viewModel.componentId)
        }
    }

    override fun setVisible(viewModel: IViewModel, isVisible: Boolean) {
        viewModel.getContainer()?.setVisible(isVisible)
    }

    override fun setFocused(viewModel: IViewModel, isFocused: Boolean) {
        viewModel.getContainer()?.setFocused(isFocused)
    }

}