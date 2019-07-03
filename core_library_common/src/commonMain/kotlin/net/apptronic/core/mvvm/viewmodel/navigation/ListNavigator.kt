package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.UpdateEntity
import net.apptronic.core.component.entity.subscriptions.ContextSubjectWrapper
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ItemStateNavigator
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter
import net.apptronic.core.threading.execute

class ListNavigator(
        private val parent: ViewModel
) : BaseListNavigator<ViewModel>(parent),
        UpdateEntity<List<ViewModel>>, VisibilityFilterableNavigator {

    private val visibilityFilters: VisibilityFilters<ViewModel> = VisibilityFilters<ViewModel>()
    private var listFilter: ListNavigatorFilter = simpleFilter()

    private val subject = BehaviorSubject<List<ViewModel>>().apply {
        update(emptyList())
    }

    private val viewModelListEntity = ContextSubjectWrapper(parent, BehaviorSubject<ListNavigatorStatus>())

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

    override fun getObservable(): Observable<List<ViewModel>> {
        return subject
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
        return containers[getId()]
    }

    fun update(action: (MutableList<ViewModel>) -> Unit) {
        uiWorker.execute {
            val list = items.toMutableList()
            action.invoke(list)
            set(list)
        }
    }

    fun set(value: List<ViewModel>) {
        uiWorker.execute {
            val diff = getDiff(items, value)
            diff.removed.forEach {
                onRemoved(it)
            }
            items = value
            diff.added.forEach {
                onAdded(it)
            }
            refreshVisibility()
            updateSubject()
        }
    }

    private fun onAdded(viewModel: ViewModel) {
        val container = ViewModelContainer(viewModel, parent, visibilityFilters.isReadyToShow(viewModel))
        containers[viewModel.getId()] = container
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
        parent.getLifecycle().onExitFromActiveStage {
            items.forEach {
                itemStateNavigator.setBound(it, false)
            }
            adapter.setNavigator(null)
        }
    }

    private inner class ItemStateNavigatorImpl : ItemStateNavigator {

        override fun setBound(viewModel: ViewModel, isBound: Boolean) {
            uiWorker.execute {
                viewModel.getContainer()?.setBound(isBound)
            }
        }

        override fun setVisible(viewModel: ViewModel, isBound: Boolean) {
            uiWorker.execute {
                viewModel.getContainer()?.setVisible(isBound)
            }
        }

        override fun setFocused(viewModel: ViewModel, isBound: Boolean) {
            uiWorker.execute {
                viewModel.getContainer()?.setFocused(isBound)
            }
        }

    }

}