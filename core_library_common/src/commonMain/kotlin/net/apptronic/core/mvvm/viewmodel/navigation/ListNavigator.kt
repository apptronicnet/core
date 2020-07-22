package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.base.UpdateEntity
import net.apptronic.core.component.entity.entities.Value
import net.apptronic.core.component.entity.entities.setAs
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.component.entity.switchContext
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ItemStateNavigator
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter

fun <T, Id, VM : ViewModel> ViewModel.listBuilder(builder: ViewModelBuilder<T, Id, VM>): ViewModelListBuilder<T, Id, VM> {
    return ViewModelListBuilder(this, builder)
}


fun ViewModel.listNavigator(navigatorContext: Context = this.context): ListNavigator {
    context.verifyNavigatorContext(navigatorContext)
    return ListNavigator(this, navigatorContext)
}

fun <T, Id, VM : ViewModel> ViewModel.listNavigator(
        source: Entity<out List<T>>,
        builder: ViewModelBuilder<T, Id, VM>,
        navigatorContext: Context = this.context
): ListNavigator {
    val listBuilder = listBuilder(builder)
    listBuilder.updateFrom(source.switchContext(navigatorContext))
    return listNavigator(navigatorContext).setAs(listBuilder)
}

fun ViewModel.listNavigator(source: Entity<List<ViewModel>>, navigatorContext: Context = this.context): ListNavigator {
    context.verifyNavigatorContext(navigatorContext)
    return ListNavigator(this, navigatorContext).apply {
        source.subscribe(context) {
            set(it)
        }
    }
}

class ListNavigator internal constructor(
        parent: ViewModel,
        override val navigatorContext: Context
) : BaseListNavigator<ViewModel>(parent),
        UpdateEntity<List<ViewModel>>, VisibilityFilterableNavigator {

    private val visibilityFilters: VisibilityFilters<ViewModel> = VisibilityFilters<ViewModel>()
    private var listFilter: ListNavigatorFilter = simpleFilter()

    private val itemStateNavigator = ItemStateNavigatorImpl()

    private var items: List<ViewModel> = emptyList()
    private var visibleItems: List<ViewModel> = emptyList()

    override val subject = BehaviorSubject<List<ViewModel>>().apply {
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
    }

    override fun getVisibilityFilters(): VisibilityFilters<ViewModel> {
        return visibilityFilters
    }

    fun setListFilter(listFilter: ListNavigatorFilter) {
        this.listFilter = listFilter
        postRefreshVisibility()
    }

    override fun update(value: List<ViewModel>) {
        set(value)
    }

    fun observeStatus(): Entity<ListNavigatorStatus> {
        return viewModelListEntity
    }

    fun getStatus(): ListNavigatorStatus {
        return ListNavigatorStatus(items, visibleItems)
    }

    override fun refreshVisibility() {
        val source = items.map {
            ItemVisibilityRequest(it, it.getContainer()?.shouldShow() ?: true)
        }
        visibleItems = listFilter.filterList(source)
        sendStatusUpdate()
        notifyAdapter()
    }

    override fun onNotifyAdapter(adapter: ViewModelListAdapter) {
        adapter.onDataChanged(visibleItems)
    }

    override fun get(): List<ViewModel> {
        return ArrayList(items)
    }

    fun getVisible(): List<ViewModel> {
        return ArrayList(visibleItems)
    }

    override fun getOrNull(): List<ViewModel>? {
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

    private fun ViewModel.getContainer(): ViewModelContainer? {
        return containers[componentId]
    }

    fun update(action: (MutableList<ViewModel>) -> Unit) {
        val list = items.toTypedArray().toMutableList()
        action.invoke(list)
        set(list)
    }

    fun set(value: List<ViewModel>) {
        val diff = getDiff(items, value)
        diff.removed.forEach {
            onRemoved(it)
        }
        items = value.toTypedArray().toList()
        diff.added.forEach {
            it.verifyContext()
            onAdded(it)
        }
        refreshVisibility()
        updateSubject()
    }

    private fun onAdded(viewModel: ViewModel) {
        val container = ViewModelContainer(viewModel, parent, visibilityFilters.isReadyToShow(viewModel))
        containers[viewModel.componentId] = container
        container.getViewModel().onAttachToParent(this)
        container.observeVisibilityChanged(::postRefreshVisibility)
        container.setAttached(true)
    }

    private fun onRemoved(viewModel: ViewModel) {
        viewModel.getContainer()?.let { container ->
            container.getViewModel().onDetachFromParent()
            container.terminate()
        }
    }

    override fun requestCloseSelf(
            viewModel: ViewModel,
            transitionInfo: Any?
    ) {
        update {
            it.remove(viewModel)
        }
    }

    override fun onSetAdapter(adapter: ViewModelListAdapter) {
        adapter.setNavigator(itemStateNavigator)
        parent.context.lifecycle.onExitFromActiveStage {
            items.forEach {
                if (boundIds.contains(it.componentId)) {
                    itemStateNavigator.setFocused(it, false)
                    itemStateNavigator.setVisible(it, false)
                    itemStateNavigator.setBound(it, false)
                }
            }
            boundIds.clear()
            adapter.setNavigator(null)
        }
    }

    private val boundIds = mutableSetOf<Long>()

    private inner class ItemStateNavigatorImpl : ItemStateNavigator {

        override fun setBound(viewModel: ViewModel, isBound: Boolean) {
            viewModel.getContainer()?.setBound(isBound)
            if (isBound) {
                boundIds.add(viewModel.componentId)
            } else {
                boundIds.remove(viewModel.componentId)
            }
        }

        override fun setVisible(viewModel: ViewModel, isVisible: Boolean) {
            viewModel.getContainer()?.setVisible(isVisible)
        }

        override fun setFocused(viewModel: ViewModel, isFocused: Boolean) {
            viewModel.getContainer()?.setFocused(isFocused)
        }

    }

}