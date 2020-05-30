package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.base.UpdateEntity
import net.apptronic.core.component.entity.subscriptions.ContextSubjectWrapper
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ItemStateNavigator
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter

class ListNavigator(
        parent: ViewModel
) : BaseListNavigator<ViewModel>(parent),
        UpdateEntity<List<ViewModel>>, VisibilityFilterableNavigator {

    private val visibilityFilters: VisibilityFilters<ViewModel> = VisibilityFilters<ViewModel>()
    private var listFilter: ListNavigatorFilter = simpleFilter()

    override val subject = BehaviorSubject<List<ViewModel>>().apply {
        update(emptyList())
    }

    private val viewModelListEntity = ContextSubjectWrapper(context, BehaviorSubject<ListNavigatorStatus>())

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

    private val itemStateNavigator = ItemStateNavigatorImpl()

    private var items: List<ViewModel> = emptyList()
    private var visibleItems: List<ViewModel> = emptyList()

    override fun refreshVisibility() {
        val source = items.map {
            ItemVisibilityRequest(it, it.getContainer()?.shouldShow() ?: true)
        }
        visibleItems = listFilter.filterList(source)
        viewModelListEntity.update(ListNavigatorStatus(items, visibleItems))
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
        return containers[id]
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
            if (it.context.parent != context) {
                throw IllegalArgumentException("$it context should be direct child of Navigator context")
            }
            onAdded(it)
        }
        refreshVisibility()
        updateSubject()
    }

    private fun onAdded(viewModel: ViewModel) {
        val container = ViewModelContainer(viewModel, parent, visibilityFilters.isReadyToShow(viewModel))
        containers[viewModel.id] = container
        container.getViewModel().onAttachToParent(this)
        container.observeVisibilityChanged(::postRefreshVisibility)
        container.setCreated(true)
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
                if (boundIds.contains(it.id)) {
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
                boundIds.add(viewModel.id)
            } else {
                boundIds.remove(viewModel.id)
            }
        }

        override fun setVisible(viewModel: ViewModel, isBound: Boolean) {
            viewModel.getContainer()?.setVisible(isBound)
        }

        override fun setFocused(viewModel: ViewModel, isBound: Boolean) {
            viewModel.getContainer()?.setFocused(isBound)
        }

    }

}