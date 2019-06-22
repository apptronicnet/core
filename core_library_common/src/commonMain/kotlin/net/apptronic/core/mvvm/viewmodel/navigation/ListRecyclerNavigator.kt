package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.base.collections.lazyListOf
import net.apptronic.core.base.collections.simpleLazyListOf
import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.UpdateEntity
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ItemStateNavigator
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter
import net.apptronic.core.threading.execute

const val RECYCLER_NAVIGATOR_DEFAULT_SAVED_ITEMS_SIZE = 10

class ListRecyclerNavigator<T : Any, Id, VM : ViewModel>(
        private val parent: ViewModel,
        private val builder: ViewModelBuilder<T, Id, VM>
) : BaseListNavigator<T>(parent),
        UpdateEntity<List<T>>, VisibilityFilterableNavigator {

    private val subject = BehaviorSubject<List<T>>().apply {
        update(emptyList())
    }
    private val status = parent.value<ListRecyclerNavigatorStatus>()

    private var visibilityFilters = VisibilityFilters<ViewModel>()
    private var listFilter: ListRecyclerNavigatorFilter = mappingFactoryFilter(::defaultMapping)
    private var indexMapping: RecyclerListIndexMapping = listFilter.filter(emptyList(), null)

    private var adapter: ViewModelListAdapter? = null
    private val itemStateNavigator = ItemStateNavigatorImpl()

    private var items: List<T> = emptyList()
    private var listDescription: Any? = null
    private var staticItems: List<T> = emptyList()
    private val viewModels = LazyList()
    private val containers = ViewModelContainers<T, Id>()

    private var savedItemsSize = RECYCLER_NAVIGATOR_DEFAULT_SAVED_ITEMS_SIZE
    private val savedItemIds = mutableListOf<Id>()

    override fun getVisibilityFilters(): VisibilityFilters<ViewModel> {
        return visibilityFilters
    }

    fun setListFilter(listFilter: ListRecyclerNavigatorFilter) {
        this.listFilter = listFilter
        postRefreshVisibility()
    }

    private fun updateSubject() {
        subject.update(items)
        updateStatusSubject()
    }

    private fun updateStatusSubject() {
        val items = this.items
        val indexMapping = this.indexMapping
        val allSize = items.size
        val visibleSize = indexMapping.getSize()
        val visibleItems = lazyListOf(items, indexMapping.getSize()) { list: List<T>, index ->
            val mappedIndex = indexMapping.mapIndex(index)
            list[mappedIndex]
        }
        val viewModels = containers.getAll().map { it.container.getViewModel() }
        status.set(ListRecyclerNavigatorStatus(
                allSize = allSize,
                visibleSize = visibleSize,
                hasHidden = visibleSize < allSize,
                allItems = items,
                visibleItems = visibleItems,
                staticItems = this.staticItems,
                attachedViewModels = viewModels.toSet()
        ))
    }

    fun getStatus(): ListRecyclerNavigatorStatus {
        return status.get()
    }

    fun observerStatus(): Entity<ListRecyclerNavigatorStatus> {
        return status
    }

    override fun update(value: List<T>) {
        set(value)
    }

    override fun getObservable(): Observable<List<T>> {
        return subject
    }

    init {
        updateStatusSubject()
    }

    override fun get(): List<T> {
        return items
    }

    override fun getOrNull(): List<T>? {
        return items
    }

    /**
     * Set source items list.
     */
    fun set(value: List<T>, listDescription: Any? = null) {
        uiWorker.execute {
            clearSaved()
            containers.markAllRequiresUpdate()
            items = value
            this.listDescription = listDescription
            adapter?.onDataChanged(viewModels)
            refreshVisibility()
            updateSubject()
        }
    }

    override fun refreshVisibility() {
        val filterable = simpleLazyListOf<T, ListItem>(
                source = items,
                mapFunction = { item ->
                    val container = containers.findRecordForId(item.getId())?.container
                    val isStatic = staticItems.contains(item)
                    val isVisible = if (isStatic) (container?.shouldShow() ?: true) else true
                    ListItem(
                            item = item,
                            viewModel = container?.getViewModel(),
                            isVisible = isVisible,
                            isStatic = isStatic
                    )
                }
        )
        indexMapping = listFilter.filter(filterable, listDescription)
        adapter?.onDataChanged(viewModels)
        updateStatusSubject()
    }

    fun setStaticItems(source: Entity<List<out T>>) {
        source.subscribe {
            setStaticItems(it)
        }
    }

    /**
     * Set list of items which is static. It means that [ViewModel]s for this items will be created immediately and
     * will not be removed when item is unbound.
     */
    fun setStaticItems(value: List<out T>) {
        uiWorker.execute {
            fun Id.getKey(): T? {
                return value.firstOrNull { key ->
                    key.getId() == this
                }
            }
            uiAsyncWorker.execute {
                val oldIds = staticItems.map { it.getId() }
                val newIds = value.map { it.getId() }
                val diff = getDiff(oldIds, newIds)
                diff.removed.forEach { id ->
                    containers.findRecordForId(id)?.let { record ->
                        if (!record.container.getViewModel().isBound()) {
                            val key = id.getKey()
                            if (key != null) {
                                onReadyToRemove(key)
                            }
                        }
                    }
                }
                staticItems = value
                diff.added.forEach { id ->
                    id.getKey()?.let {
                        onAdded(it)
                    }
                }
                refreshVisibility()
            }
        }
    }

    private fun shouldRetainInstance(key: T, viewModel: ViewModel): Boolean {
        return staticItems.contains(key) || builder.shouldRetainInstance(key, viewModel as VM)
    }

    private fun T.getId(): Id {
        return builder.getId(this)
    }

    private fun onAdded(key: T): ViewModelContainer {
        val viewModel = builder.onCreateViewModel(parent, key)
        val container = ViewModelContainer(viewModel, parent, visibilityFilters.isReadyToShow(viewModel))
        containers.add(key.getId(), container, key)
        container.getViewModel().onAttachToParent(this)
        container.observeVisibilityChanged {
            if (staticItems.contains(key)) {
                postRefreshVisibility()
            }
        }
        container.setCreated(true)
        updateStatusSubject()
        return container
    }

    private fun onReadyToRemove(key: T) {
        savedItemIds.add(key.getId())
        reduceSaved()
    }

    /**
     * Set count of items which [ViewModel]s will be saved when unbound to prevent too frequent recreation
     * of [ViewModel]s. It is in addition to static items which are retained always.
     * Default value is [RECYCLER_NAVIGATOR_DEFAULT_SAVED_ITEMS_SIZE]
     */
    fun setSavedItemsSize(size: Int) {
        savedItemsSize = size
        reduceSaved()
    }

    private fun reduceSaved() {
        while (savedItemIds.size > savedItemsSize) {
            val id = savedItemIds.removeAt(0)
            onRemovedById(id)
        }
    }

    private fun clearSaved() {
        savedItemIds.forEach {
            onRemovedById(it)
        }
        savedItemIds.clear()
    }

    fun getViewModelForItem(item: T): VM? {
        return getViewModelForId(item.getId())
    }

    fun getViewModelForId(id: Id): VM? {
        return containers.findRecordForId(id)?.container?.getViewModel() as? VM
    }

    private fun onRemoved(key: T) {
        onRemovedById(key.getId())
    }

    private fun onRemovedById(id: Id) {
        containers.removeById(id)?.let { container ->
            container.container.getViewModel().onDetachFromParent()
            container.container.terminate()
        }
        updateStatusSubject()
    }

    override fun requestCloseSelf(
            viewModel: ViewModel,
            transitionInfo: Any?
    ) {
        throw UnsupportedOperationException("ListRecyclerNavigator cannot close items")
    }

    override fun setAdapter(adapter: ViewModelListAdapter) {
        uiWorker.execute {
            this.adapter = adapter
            adapter.onDataChanged(viewModels)
            adapter.setNavigator(itemStateNavigator)
            parent.getLifecycle().onExitFromActiveStage {
                adapter.setNavigator(null)
                this.adapter = null
                containers.getAll().forEach {
                    onRemoved(it.item)
                }
                containers.terminateAllAndClear()
            }
        }
    }

    private inner class LazyList : AbstractList<ViewModel>() {

        override val size: Int
            get() {
                return indexMapping.getSize()
            }

        override fun get(index: Int): ViewModel {
            val mappedIndex = indexMapping.mapIndex(index)
            val key = items[mappedIndex]
            val existing = containers.findRecordForId(key.getId())
            return if (existing == null) {
                onAdded(key).getViewModel()
            } else {
                savedItemIds.remove(key.getId())
                if (existing.requiresUpdate) {
                    existing.item = key
                    builder.onUpdateViewModel(existing.container.getViewModel() as VM, key)
                    existing.requiresUpdate = false
                }
                existing.container.getViewModel()
            }
        }

        override fun indexOf(element: ViewModel): Int {
            // not applicable
            return -1
        }

        override fun lastIndexOf(element: ViewModel): Int {
            // not applicable
            return -1
        }

    }

    private inner class ItemStateNavigatorImpl : ItemStateNavigator {

        override fun setBound(viewModel: ViewModel, isBound: Boolean) {
            uiWorker.execute {
                if (isBound) {
                    containers.findRecordForModel(viewModel)?.let { record ->
                        record.container.setBound(true)
                    }
                } else {
                    containers.findRecordForModel(viewModel)?.let { record ->
                        record.container.setBound(false)
                        if (!shouldRetainInstance(record.item, viewModel as VM)) {
                            onReadyToRemove(record.item)
                        }
                    }
                }
            }
        }

        override fun setVisible(viewModel: ViewModel, isBound: Boolean) {
            uiWorker.execute {
                containers.findRecordForModel(viewModel)?.container?.setVisible(isBound)
            }
        }

        override fun setFocused(viewModel: ViewModel, isBound: Boolean) {
            uiWorker.execute {
                containers.findRecordForModel(viewModel)?.container?.setFocused(isBound)
            }
        }

    }

}