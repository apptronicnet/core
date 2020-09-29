package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.base.collections.wrapLists
import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.base.UpdateEntity
import net.apptronic.core.component.entity.entities.Value
import net.apptronic.core.component.entity.entities.setAs
import net.apptronic.core.component.entity.onchange.Next
import net.apptronic.core.component.entity.onchange.OnChangeProperty
import net.apptronic.core.component.entity.onchange.takeValue
import net.apptronic.core.component.entity.switchContext
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter

fun <T, Id, VM : IViewModel> IViewModel.listBuilder(builder: ViewModelBuilder<T, Id, VM>): ViewModelListBuilder<T, Id, VM> {
    return viewModelListBuilder(builder)
}

fun <T, Id, VM : IViewModel> ListNavigator.listBuilder(builder: ViewModelBuilder<T, Id, VM>): ViewModelListBuilder<T, Id, VM> {
    return navigatorContext.viewModelListBuilder(builder)
}

fun IViewModel.listNavigator(navigatorContext: Context = this.context): ListNavigator {
    context.verifyNavigatorContext(navigatorContext)
    return ListNavigator(this, navigatorContext)
}

fun <T, Id, VM : IViewModel> IViewModel.listNavigator(
        source: Entity<out List<T>>,
        builder: ViewModelBuilder<T, Id, VM>,
        navigatorContext: Context = this.context
): ListNavigator {
    val listBuilder = listBuilder(builder)
    listBuilder.updateFrom(source.switchContext(navigatorContext))
    return listNavigator(navigatorContext).setAs(listBuilder.takeValue())
}

fun <T, Id, VM : IViewModel> IViewModel.listNavigatorOnChange(
        source: Entity<Next<out List<T>, out Any>>,
        builder: ViewModelBuilder<T, Id, VM>,
        navigatorContext: Context = this.context
): ListNavigator {
    val listBuilder = listBuilder(builder)
    listBuilder.updateFromChanges(source.switchContext(navigatorContext))
    return listNavigator(navigatorContext).setAs(listBuilder)
}

fun IViewModel.listNavigator(source: Entity<List<IViewModel>>, navigatorContext: Context = this.context): ListNavigator {
    context.verifyNavigatorContext(navigatorContext)
    return ListNavigator(this, navigatorContext).apply {
        source.subscribe(context) {
            set(it)
        }
    }
}

fun IViewModel.listNavigatorOnChange(source: Entity<Next<List<IViewModel>, Any>>, navigatorContext: Context = this.context): ListNavigator {
    context.verifyNavigatorContext(navigatorContext)
    return ListNavigator(this, navigatorContext).apply {
        source.subscribe(context) {
            set(it.value, it.change)
        }
    }
}

class ListNavigator internal constructor(
        parent: IViewModel,
        override val navigatorContext: Context
) : BaseListNavigator<IViewModel>(parent),
        UpdateEntity<List<IViewModel>>, VisibilityFilterableNavigator {

    private val visibilityFilters: VisibilityFilters<IViewModel> = VisibilityFilters<IViewModel>()
    private var listFilter: ListNavigatorFilter = simpleFilter()

    private var items: List<IViewModel> = emptyList()
    private var visibleItems: List<IViewModel> = emptyList()

    override val subject = BehaviorSubject<List<IViewModel>>().apply {
        update(emptyList())
    }

    private val viewModelListEntity = Value<ListNavigatorStatus>(context)

    private fun sendStatusUpdate() {
        viewModelListEntity.set(ListNavigatorStatus(items, visibleItems))
    }

    init {
        sendStatusUpdate()
    }

    private fun updateSubject() {
        subject.update(items)
        sendStatusUpdate()
    }

    override fun getVisibilityFilters(): VisibilityFilters<IViewModel> {
        return visibilityFilters
    }

    fun setListFilter(listFilter: ListNavigatorFilter) {
        this.listFilter = listFilter
        refreshVisibility(null)
    }

    override fun update(value: List<IViewModel>) {
        set(value)
    }

    fun observeStatus(): Entity<ListNavigatorStatus> {
        return viewModelListEntity
    }

    fun getStatus(): ListNavigatorStatus {
        return ListNavigatorStatus(items, visibleItems)
    }

    fun setAs(source: OnChangeProperty<List<IViewModel>, out Any>): ListNavigator {
        source.subscribe {
            set(it.value, it.change)
        }
        return this
    }

    private fun refreshVisibility(changeInfo: Any?) {
        val source = items.map {
            ItemVisibilityRequest(it, it.getContainer()?.shouldShow() ?: true)
        }
        visibleItems = listFilter.filterList(source)
        sendStatusUpdate()
        notifyAdapter(changeInfo)
    }

    override fun onNotifyAdapter(adapter: ViewModelListAdapter, changeInfo: Any?) {
        adapter.onDataChanged(visibleItems, changeInfo)
    }

    override fun get(): List<IViewModel> {
        return ArrayList(items)
    }

    fun getVisible(): List<IViewModel> {
        return ArrayList(visibleItems)
    }

    override fun getOrNull(): List<IViewModel>? {
        return ArrayList(items)
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

    fun update(changeInfo: Any? = null, action: (MutableList<IViewModel>) -> Unit) {
        val list = items.toTypedArray().toMutableList()
        action.invoke(list)
        set(list, changeInfo)
    }

    fun set(value: List<IViewModel>, changeInfo: Any? = null) {
        val diff = getDiff(items, value)
        diff.removed.forEach {
            onRemoved(it)
        }
        items = value.toTypedArray().toList()
        diff.added.forEach {
            it.verifyContext()
            onAdded(it)
        }
        refreshVisibility(changeInfo)
        updateSubject()
    }

    private fun onAdded(viewModel: IViewModel) {
        val container = ViewModelContainer(viewModel, parent, visibilityFilters.isReadyToShow(viewModel))
        containers[viewModel.componentId] = container
        container.getViewModel().onAttachToParent(this)
        container.observeVisibilityChanged {
            if (it) {
                refreshVisibility(ItemAdded(indexOfViewModel(viewModel)))
            } else {
                refreshVisibility(ItemRemoved(indexOfViewModel(viewModel)))
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

    override fun requestCloseSelf(
            viewModel: IViewModel,
            transitionInfo: Any?
    ) {
        update {
            it.remove(viewModel)
        }
    }

    override fun onSetAdapter(adapter: ViewModelListAdapter) {
        parent.context.lifecycle.onExitFromActiveStage {
            items.forEach {
                if (boundIds.contains(it.componentId)) {
                    setFocused(it, false)
                    setVisible(it, false)
                    setBound(it, false)
                }
            }
            boundIds.clear()
        }
    }

    private val boundIds = mutableSetOf<Long>()

    override fun getSize(): Int {
        return visibleItems.size
    }

    override fun getViewModels(): List<IViewModel> {
        return wrapLists(visibleItems)
    }

    override fun indexOfViewModel(viewModel: IViewModel): Int {
        return visibleItems.indexOf(viewModel)
    }

    override fun getViewModelAt(index: Int): IViewModel {
        return visibleItems[index]
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