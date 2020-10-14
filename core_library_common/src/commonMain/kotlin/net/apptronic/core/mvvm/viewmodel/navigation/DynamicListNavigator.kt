package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.base.collections.lazyListOf
import net.apptronic.core.base.collections.simpleLazyListOf
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.value
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter
import kotlin.reflect.KClass

private const val DEFAULT_SAVED_ITEMS_SIZE = 10

@Suppress("UNCHECKED_CAST")
abstract class DynamicListNavigator<T : Any, Id : Any, VM : IViewModel, State>(
        parent: IViewModel,
        private val builder: ViewModelBuilder<in T, in Id, in VM>,
        override val navigatorContext: Context,
        initialState: State
) : BaseListNavigator<DynamicListNavigatorStatus<T, State>, State>(parent), VisibilityFilterableNavigator {

    private data class RecyclerItemId(
            val clazz: KClass<*>,
            val typeId: Any
    )

    private var visibilityFilters = VisibilityFilters<IViewModel>()
    private var listFilter: ListRecyclerNavigatorFilter = mappingFactoryFilter(::defaultMapping)
    private var indexMapping: DynamicListIndexMapping = listFilter.filter(emptyList(), null)

    private var items: List<T> = emptyList()
    private var listDescription: Any? = null
    private var staticItems: List<T> = emptyList()
    private val viewModels = LazyList()
    private val containers = ViewModelContainers<T, RecyclerItemId>()

    private var savedItemsSize = DEFAULT_SAVED_ITEMS_SIZE
    private val savedItemIds = mutableListOf<RecyclerItemId>()

    private val contentData = parent.value<DynamicListNavigatorStatus<T, State>>()

    override val content = contentData

    override fun getVisibilityFilters(): VisibilityFilters<IViewModel> {
        return visibilityFilters
    }

    fun setListFilter(listFilter: ListRecyclerNavigatorFilter) {
        this.listFilter = listFilter
        refreshVisibility(true)
    }

    private fun refreshContentData(state: State) {
        val items = this.items
        val indexMapping = this.indexMapping
        val allSize = items.size
        val visibleSize = indexMapping.getSize()
        val visibleItems = lazyListOf(items, indexMapping.getSize()) { list: List<T>, index ->
            val mappedIndex = indexMapping.mapIndex(index)
            list[mappedIndex]
        }
        val viewModels = containers.getAll().map { it.container.getViewModel() }
        contentData.set(
                DynamicListNavigatorStatus(
                        allSize = allSize,
                        visibleSize = visibleSize,
                        hasHidden = visibleSize < allSize,
                        allItems = items,
                        visibleItems = visibleItems,
                        staticItems = this.staticItems,
                        attachedViewModels = viewModels.toSet(),
                        state = state
                )
        )
    }

    init {
        refreshContentData(initialState)
    }

    fun get(): List<T> {
        return items
    }

    /**
     * Set source items list.
     */
    fun set(value: List<T>, state: State, changeInfo: Any? = null, listDescription: Any? = null) {
        clearSaved()
        containers.markAllRequiresUpdate()
        items = value
        this.listDescription = listDescription
        refreshVisibility(false)
        refreshContentData(state)
        notifyAdapter(changeInfo)
    }

    override fun onNotifyAdapter(adapter: ViewModelListAdapter<State>, changeInfo: Any?) {
        adapter.onDataChanged(viewModels, contentData.get().state, changeInfo)
    }

    private fun refreshVisibility(notifyChanges: Boolean) {
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
        if (notifyChanges) {
            refreshContentData(contentData.get().state)
            notifyAdapter(null)
        }
    }

    fun setStaticItems(source: Entity<List<T>>) {
        source.subscribe(context) {
            setStaticItems(it)
        }
    }

    /**
     * Set list of items which is static. It means that [ViewModel]s for this items will be created immediately and
     * will not be removed when item is unbound.
     */
    fun setStaticItems(value: List<T>) {
        fun RecyclerItemId.getKey(): T? {
            return value.firstOrNull { key ->
                key.getId() == this
            }
        }

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
        refreshVisibility(true)
    }

    private fun shouldRetainInstance(key: T, viewModel: IViewModel): Boolean {
        return staticItems.contains(key) || builder.shouldRetainInstance(key, viewModel as VM)
    }

    private fun T.getId(): RecyclerItemId {
        val typeId = builder.getId(this) as Any
        return RecyclerItemId(this::class, typeId)
    }

    private fun onAdded(key: T): ViewModelContainer {
        val viewModel: IViewModel = builder.onCreateViewModel(navigatorContext, key) as IViewModel
        viewModel.verifyContext()
        val container = ViewModelContainer(viewModel, parent, visibilityFilters.isReadyToShow(viewModel))
        containers.add(key.getId(), container, key)
        container.getViewModel().onAttachToParent(this)
        container.observeVisibilityChanged {
            if (staticItems.contains(key)) {
                refreshVisibility(true)
            }
        }
        container.setAttached(true)
        refreshContentData(contentData.get().state)
        return container
    }

    private fun onReadyToRemove(key: T) {
        savedItemIds.add(key.getId())
        reduceSaved()
    }

    /**
     * Set count of items which [ViewModel]s will be saved when unbound to prevent too frequent recreation
     * of [ViewModel]s. It is in addition to static items which are retained always.
     * Default value is [DEFAULT_SAVED_ITEMS_SIZE]
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

    private fun getViewModelForId(id: RecyclerItemId): VM? {
        return containers.findRecordForId(id)?.container?.getViewModel() as? VM
    }

    private fun onRemoved(key: T) {
        onRemovedById(key.getId())
    }

    private fun onRemovedById(id: RecyclerItemId) {
        containers.removeById(id)?.let { container ->
            container.container.getViewModel().onDetachFromParent()
            container.container.terminate()
        }
        refreshContentData(contentData.get().state)
    }

    override fun requestCloseSelf(
            viewModel: IViewModel,
            transitionInfo: Any?
    ) {
        throw UnsupportedOperationException("ListRecyclerNavigator cannot close items")
    }

    override fun onSetAdapter(adapter: ViewModelListAdapter<State>) {
        parent.context.lifecycle.onExitFromActiveStage {
            containers.getAll().forEach {
                onRemoved(it.item)
            }
            containers.terminateAllAndClear()
        }
    }

    private inner class LazyList : AbstractList<ViewModelItem>() {

        override val size: Int
            get() {
                return indexMapping.getSize()
            }

        override fun get(index: Int): ViewModelItem {
            val mappedIndex = indexMapping.mapIndex(index)
            val key = items[mappedIndex]
            val existing = containers.findRecordForId(key.getId())
            val container = if (existing == null) {
                onAdded(key)
            } else {
                savedItemIds.remove(key.getId())
                if (existing.requiresUpdate) {
                    existing.item = key
                    builder.onUpdateViewModel(existing.container.getViewModel() as VM, key)
                    existing.requiresUpdate = false
                }
                existing.container
            }
            return ViewModelItem(container, this@DynamicListNavigator)
        }

        override fun indexOf(element: ViewModelItem): Int {
            // not applicable
            return -1
        }

        override fun lastIndexOf(element: ViewModelItem): Int {
            // not applicable
            return -1
        }

    }

    override fun getSize(): Int {
        return viewModels.size
    }

    override fun getState(): State {
        return contentData.get().state
    }

    override fun getViewModelItemAt(index: Int): ViewModelItem {
        return viewModels[index]
    }

    override fun getViewModelItems(): List<ViewModelItem> {
        return viewModels
    }

    override fun indexOfViewModel(viewModel: IViewModel): Int {
        return -1
    }

    override fun setBound(viewModel: IViewModel, isBound: Boolean) {
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

    override fun setVisible(viewModel: IViewModel, isVisible: Boolean) {
        containers.findRecordForModel(viewModel)?.container?.setVisible(isVisible)
    }

    override fun setFocused(viewModel: IViewModel, isFocused: Boolean) {
        containers.findRecordForModel(viewModel)?.container?.setFocused(isFocused)
    }

}