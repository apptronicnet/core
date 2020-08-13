package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.base.collections.lazyListOf
import net.apptronic.core.base.collections.simpleLazyListOf
import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.base.UpdateEntity
import net.apptronic.core.component.entity.onchange.Next
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.component.value
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter
import kotlin.reflect.KClass

private const val DEFAULT_SAVED_ITEMS_SIZE = 10

fun <T : Any, Id : Any, VM : ViewModel> ViewModel.listDynamicNavigator(
        builder: ViewModelBuilder<in T, in Id, in VM>, navigatorContext: Context = this.context
): DynamicListNavigator<T, Id, VM> {
    context.verifyNavigatorContext(navigatorContext)
    return DynamicListNavigator(this, builder, navigatorContext)
}

fun <T : Any, Id : Any, VM : ViewModel> ViewModel.listDynamicNavigator(
        source: Entity<List<T>>,
        builder: ViewModelBuilder<in T, in Id, in VM>,
        navigatorContext: Context = this.context
): DynamicListNavigator<T, Id, VM> {
    val navigator = listDynamicNavigator(builder, navigatorContext)
    source.subscribe(navigatorContext) {
        navigator.set(it)
    }
    return navigator
}

fun <T : Any, Id : Any, VM : ViewModel> ViewModel.listDynamicNavigatorOnChange(
        source: Entity<Next<List<T>, Any?>>,
        builder: ViewModelBuilder<in T, in Id, in VM>,
        navigatorContext: Context = this.context
): DynamicListNavigator<T, Id, VM> {
    val navigator = listDynamicNavigator(builder, navigatorContext)
    source.subscribe(navigatorContext) {
        navigator.set(it.value, it.change)
    }
    return navigator
}

@Suppress("UNCHECKED_CAST")
class DynamicListNavigator<T : Any, Id : Any, VM : ViewModel> internal constructor(
        parent: ViewModel,
        private val builder: ViewModelBuilder<in T, in Id, in VM>,
        override val navigatorContext: Context
) : BaseListNavigator<T>(parent), UpdateEntity<List<T>>, VisibilityFilterableNavigator {

    private data class RecyclerItemId(
            val clazz: KClass<*>,
            val typeId: Any
    )

    override val subject = BehaviorSubject<List<T>>().apply {
        update(emptyList())
    }
    private val status = parent.value<ListRecyclerNavigatorStatus>()

    private var visibilityFilters = VisibilityFilters<ViewModel>()
    private var listFilter: ListRecyclerNavigatorFilter = mappingFactoryFilter(::defaultMapping)
    private var indexMapping: DynamicListIndexMapping = listFilter.filter(emptyList(), null)

    private var items: List<T> = emptyList()
    private var listDescription: Any? = null
    private var staticItems: List<T> = emptyList()
    private val viewModels = LazyList()
    private val containers = ViewModelContainers<T, RecyclerItemId>()

    private var savedItemsSize = DEFAULT_SAVED_ITEMS_SIZE
    private val savedItemIds = mutableListOf<RecyclerItemId>()

    override fun getVisibilityFilters(): VisibilityFilters<ViewModel> {
        return visibilityFilters
    }

    fun setListFilter(listFilter: ListRecyclerNavigatorFilter) {
        this.listFilter = listFilter
        refreshVisibility(true)
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
    fun set(value: List<T>, changeInfo: Any? = null, listDescription: Any? = null) {
        clearSaved()
        containers.markAllRequiresUpdate()
        items = value
        this.listDescription = listDescription
        refreshVisibility(false)
        updateSubject()
        notifyAdapter(changeInfo)
    }

    override fun onNotifyAdapter(adapter: ViewModelListAdapter, changeInfo: Any?) {
        adapter.onDataChanged(viewModels, changeInfo)
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
            updateStatusSubject()
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

    private fun shouldRetainInstance(key: T, viewModel: ViewModel): Boolean {
        return staticItems.contains(key) || builder.shouldRetainInstance(key, viewModel as VM)
    }

    private fun T.getId(): RecyclerItemId {
        val typeId = builder.getId(this) as Any
        return RecyclerItemId(this::class, typeId)
    }

    private fun onAdded(key: T): ViewModelContainer {
        val viewModel: ViewModel = builder.onCreateViewModel(navigatorContext, key) as ViewModel
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
        updateStatusSubject()
    }

    override fun requestCloseSelf(
            viewModel: ViewModel,
            transitionInfo: Any?
    ) {
        throw UnsupportedOperationException("ListRecyclerNavigator cannot close items")
    }

    override fun onSetAdapter(adapter: ViewModelListAdapter) {
        parent.context.lifecycle.onExitFromActiveStage {
            containers.getAll().forEach {
                onRemoved(it.item)
            }
            containers.terminateAllAndClear()
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

    override fun getSize(): Int {
        return viewModels.size
    }

    override fun getViewModelAt(index: Int): ViewModel {
        return viewModels[index]
    }

    override fun getViewModels(): List<ViewModel> {
        return viewModels
    }

    override fun indexOfViewModel(viewModel: ViewModel): Int {
        return viewModels.indexOf(viewModel)
    }

    override fun setBound(viewModel: ViewModel, isBound: Boolean) {
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

    override fun setVisible(viewModel: ViewModel, isVisible: Boolean) {
        containers.findRecordForModel(viewModel)?.container?.setVisible(isVisible)
    }

    override fun setFocused(viewModel: ViewModel, isFocused: Boolean) {
        containers.findRecordForModel(viewModel)?.container?.setFocused(isFocused)
    }

}