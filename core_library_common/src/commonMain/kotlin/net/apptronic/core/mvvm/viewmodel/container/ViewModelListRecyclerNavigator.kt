package net.apptronic.core.mvvm.viewmodel.container

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.component.entity.UpdateEntity
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ItemStateNavigator
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter
import net.apptronic.core.threading.execute

const val RECYCLER_NAVIGATOR_DEFAULT_SAVED_ITEMS_SIZE = 10

class ViewModelListRecyclerNavigator<T : Any, Id, VM : ViewModel>(
        private val parent: ViewModel,
        private val builder: ViewModelBuilder<T, Id, VM>
) : BaseViewModelListNavigator<T>(parent),
        UpdateEntity<List<T>> {

    private val subject = BehaviorSubject<List<T>>().apply {
        update(emptyList())
    }

    private fun updateSubject() {
        subject.update(items)
    }

    override fun update(value: List<T>) {
        set(value)
    }

    override fun getObservable(): Observable<List<T>> {
        return subject
    }

    private var adapter: ViewModelListAdapter? = null
    private val itemStateNavigator = ItemStateNavigatorImpl()

    private var items: List<T> = emptyList()
    private var staticItems: List<T> = emptyList()
    private val viewModels = LazyList()
    private val containers = ViewModelContainers<T, Id>()

    private var savedItemsSize = RECYCLER_NAVIGATOR_DEFAULT_SAVED_ITEMS_SIZE
    private val savedItemIds = mutableListOf<Id>()

    override fun get(): List<T> {
        return items
    }

    override fun getOrNull(): List<T>? {
        return items
    }

    /**
     * Set source items list.
     */
    fun set(value: List<T>) {
        uiWorker.execute {
            clearSaved()
            containers.markAllRequiresUpdate()
            items = value
            adapter?.onDataChanged(viewModels)
            updateSubject()
        }
    }

    /**
     * Set list of items which is static. It means that [ViewModel]s for this items will be created immediately and
     * will not be removed when item is unbound.
     */
    fun setStaticItems(value: List<T>) {
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
                                onRemoved(key)
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
            }
        }
    }

    private fun shouldRetainInstance(key: T, viewModel: ViewModel): Boolean {
        return staticItems.contains(key) || builder.shouldRetainInstance(key, viewModel as VM)
    }

    private fun T.getId(): Id {
        return builder.getId(this)
    }

    private fun onAdded(key: T): ViewModelContainerItem {
        val viewModel = builder.onCreateViewModel(parent, key)
        val container = ViewModelContainerItem(viewModel, parent)
        containers.add(key.getId(), container, key)
        container.getViewModel().onAttachToParent(this)
        container.setCreated(true)
        return container
    }

    private fun onUnbound(key: T) {
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

    private fun onRemoved(key: T) {
        onRemovedById(key.getId())
    }

    private fun onRemovedById(id: Id) {
        containers.removeById(id)?.let { container ->
            container.container.getViewModel().onDetachFromParent()
            container.container.terminate()
        }
    }

    override fun requestCloseSelf(
            viewModel: ViewModel,
            transitionInfo: Any?
    ) {
        throw UnsupportedOperationException("ViewModelListRecyclerNavigator cannot close items")
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
                containers.clear()
            }
        }
    }

    private inner class LazyList : AbstractList<ViewModel>() {

        override val size: Int
            get() = items.size

        override fun get(index: Int): ViewModel {
            val key = items[index]
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
            return -1
        }

        override fun lastIndexOf(element: ViewModel): Int {
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
                        if (shouldRetainInstance(record.item, viewModel as VM)) {
                            record.container.setBound(false)
                        } else {
                            onUnbound(record.item)
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