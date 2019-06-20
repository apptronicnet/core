package net.apptronic.core.mvvm.viewmodel.container

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.UpdateEntity
import net.apptronic.core.component.entity.subscriptions.ContextSubjectWrapper
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ItemStateNavigator
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter
import net.apptronic.core.threading.execute

class ViewModelListNavigator(
        private val parent: ViewModel
) : BaseViewModelListNavigator<ViewModel>(parent),
        UpdateEntity<List<ViewModel>> {

    private var postRefreshingVisibility = false

    private val filters = VisibilityFilters<ViewModel>()
    private var listFilter: ViewModelListFilter = simpleFilter()

    private val subject = BehaviorSubject<List<ViewModel>>().apply {
        update(emptyList())
    }

    private val viewModelListEntity = ContextSubjectWrapper(parent, BehaviorSubject<ViewModelList>())

    private fun updateSubject() {
        subject.update(items)
    }

    fun getFilters(): VisibilityFilters<ViewModel> {
        return filters
    }

    fun setListFilter(listFilter: ViewModelListFilter) {
        this.listFilter = listFilter
        postRefreshVisibility()
    }

    override fun update(value: List<ViewModel>) {
        set(value)
    }

    override fun getObservable(): Observable<List<ViewModel>> {
        return subject
    }

    fun observeList(): Entity<ViewModelList> {
        return viewModelListEntity
    }

    fun getList(): ViewModelList {
        return ViewModelList(items, visibleItems)
    }

    private var adapter: ViewModelListAdapter? = null
    private val itemStateNavigator = ItemStateNavigatorImpl()

    private var items: List<ViewModel> = emptyList()
    private var visibleItems: List<ViewModel> = emptyList()

    private fun postRefreshVisibility() {
        if (!postRefreshingVisibility) {
            postRefreshingVisibility = true
            uiAsyncWorker.execute {
                refreshVisibility()
                postRefreshingVisibility = false
            }
        }
    }

    private fun refreshVisibility() {
        val source = items.map {
            ViewModelVisibilityRequest(it, it.getContainer()?.shouldShow() ?: true)
        }
        visibleItems = listFilter.filterList(source)
        adapter?.onDataChanged(visibleItems)
        viewModelListEntity.update(ViewModelList(items, visibleItems))
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

    private val containers = mutableMapOf<Long, ViewModelContainerItem>()

    private fun ViewModel.getContainer(): ViewModelContainerItem? {
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
        val diff = getDiff(items, value)
        diff.removed.forEach {
            onRemoved(it)
        }
        items = value
        diff.added.forEach {
            onAdded(it)
        }
        postRefreshVisibility()
    }

    private fun onAdded(viewModel: ViewModel) {
        val container = ViewModelContainerItem(viewModel, parent, filters.shouldShow(viewModel), this::postRefreshVisibility)
        containers[viewModel.getId()] = container
        container.getViewModel().onAttachToParent(this)
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

    override fun setAdapter(adapter: ViewModelListAdapter) {
        uiWorker.execute {
            this.adapter = adapter
            adapter.onDataChanged(visibleItems)
            adapter.setNavigator(itemStateNavigator)
            parent.getLifecycle().onExitFromActiveStage {
                items.forEach {
                    itemStateNavigator.setBound(it, false)
                }
                adapter.setNavigator(null)
                this.adapter = null
            }
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