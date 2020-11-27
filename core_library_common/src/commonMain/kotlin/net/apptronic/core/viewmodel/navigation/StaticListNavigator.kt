package net.apptronic.core.viewmodel.navigation

import net.apptronic.core.context.Context
import net.apptronic.core.entity.commons.value
import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.navigation.adapters.ViewModelListAdapter
import net.apptronic.core.viewmodel.navigation.models.StaticListNavigatorContent

abstract class StaticListNavigator<State> internal constructor(
        parent: IViewModel,
        final override val navigatorContext: Context,
        state: State
) : ListNavigator<StaticListNavigatorContent<State>, IViewModel, State>(parent), VisibilityFilterableNavigator {

    private val visibilityFilters: VisibilityFilters<IViewModel> = VisibilityFilters<IViewModel>()
    private var listFilter: ListNavigatorFilter = simpleFilter()

    private var items: List<ViewModelContainer> = emptyList()
    private var visibleItems: List<ViewModelContainer> = emptyList()

    private val contentData = parent.value<StaticListNavigatorContent<State>>()

    override val content = contentData

    private fun sendStatusUpdate(state: State) {
        contentData.set(StaticListNavigatorContent(items.map { it.getViewModel() }, visibleItems.map { it.getViewModel() }, state))
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

    private fun refreshVisibility(state: State, updateSpec: Any?) {
        refreshVisibilityWithChange(state, { updateSpec })
    }

    private fun refreshVisibilityWithChange(state: State, updateSpecProvider: () -> Any?) {
        val source = items.map {
            ItemVisibilityRequest(it.getViewModel(), it.shouldShow())
        }
        val filterResult = listFilter.filterList(source)
        visibleItems = items.filter {
            filterResult.contains(it.getViewModel())
        }
        val filteredState = calculateFilteredState(items.map { it.getViewModel() }, filterResult, state)
        sendStatusUpdate(filteredState)
        val updateSpec = updateSpecProvider()
        notifyAdapter(updateSpec)
    }

    protected abstract fun calculateFilteredState(all: List<IViewModel>, visible: List<IViewModel>, state: State): State

    override fun onNotifyAdapter(adapter: ViewModelListAdapter<in State>, updateSpec: Any?) {
        adapter.onDataChanged(visibleItems.map { ViewModelItem(it, viewModelItemLifecycleController) }, contentData.get().state, updateSpec)
    }

    override val size: Int
        get() = items.size

    override val state: State
        get() = content.get().state

    override fun getItemAt(index: Int): IViewModel {
        return items[index].getViewModel()
    }

    override fun getItems(): List<IViewModel> {
        return items.map { it.getViewModel() }
    }

    override fun getViewModelAt(index: Int): IViewModel {
        return items[index].getViewModel()
    }

    override fun getViewModels(): List<IViewModel> {
        return items.map { it.getViewModel() }
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

    fun update(state: State, updateSpec: Any? = null, action: (MutableList<IViewModel>) -> Unit) {
        val list = items.map { it.getViewModel() }.toMutableList()
        action.invoke(list)
        set(list, state, updateSpec)
    }

    override fun updateState(state: State, updateSpec: Any?) {
        refreshVisibility(state, updateSpec)
    }

    override fun set(items: List<IViewModel>, state: State, updateSpec: Any?) {
        val diff = getDiff(this.items.map { it.getViewModel() }, items)
        diff.removed.forEach {
            onRemoved(it)
        }
        val newVideModels = items.toTypedArray().toList()
        diff.added.forEach {
            it.verifyContext()
            onAdded(it)
        }
        this.items = newVideModels.mapNotNull { it.getContainer() }
        refreshVisibility(state, updateSpec)
    }

    private fun indexOfViewModel(viewModel: IViewModel): Int {
        return visibleItems.indexOfFirst { it.getViewModel() == viewModel }
    }

    private fun onAdded(viewModel: IViewModel) {
        val container = ViewModelContainer(viewModel, parent, visibilityFilters.isReadyToShow(viewModel))
        containers[viewModel.componentId] = container
        container.getViewModel().onAttachToParent(this)
        container.observeVisibilityChanged {
            if (it) {
                refreshVisibilityWithChange(contentData.get().state) {
                    val index = indexOfViewModel(viewModel)
                    return@refreshVisibilityWithChange if (index >= 0) {
                        ListUpdateSpec.ItemAdded(indexOfViewModel(viewModel))
                    } else null
                }
            } else {
                val index = indexOfViewModel(viewModel)
                val updateSpec = if (index >= 0) {
                    ListUpdateSpec.ItemRemoved(index)
                } else null
                refreshVisibility(contentData.get().state, updateSpec)
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

    override fun onSetAdapter(adapter: ViewModelListAdapter<in State>) {
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

    private val viewModelItemLifecycleController: ViewModelItemLifecycleController = object : ViewModelItemLifecycleController {

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

}