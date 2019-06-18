package net.apptronic.core.mvvm.viewmodel.container

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.component.entity.UpdateEntity
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter
import net.apptronic.core.threading.execute

class ViewModelListRecyclerNavigator<T : Any, Id, VM : ViewModel>(
        private val parent: ViewModel,
        private val builder: ViewModelBuilder<T, Id, VM>
) : BaseViewModelListNavigator<T>(parent),
        UpdateEntity<List<T>>,
        ViewModelListAdapter.SourceNavigator {

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

    private var items: List<T> = emptyList()
    private var staticItems: List<T> = emptyList()
    private val viewModels = LazyList()

    override fun get(): List<T> {
        return items
    }

    override fun getOrNull(): List<T>? {
        return items
    }

    private val containers = ViewModelContainers<T, Id>()

    fun set(value: List<T>) {
        uiAsyncWorker.execute {
            containers.markAllRequiresUpdate()
            items = value
            adapter?.onDataChanged(viewModels)
            updateSubject()
        }
    }

    fun setStaticItems(value: List<T>) {
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
                    if (!record.container.viewModel.isBound()) {
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
        container.viewModel.onAttachToParent(this)
        container.setCreated(true)
        return container
    }

    private fun onRemoved(key: T) {
        containers.removeById(key.getId())?.let { container ->
            container.container.viewModel.onDetachFromParent()
            container.container.terminate()
        }
    }

    override fun requestCloseSelf(
            viewModel: ViewModel,
            transitionInfo: Any?
    ) {
        throw UnsupportedOperationException("ViewModelListRecyclerNavigator cannot close items")
    }

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
                        onRemoved(record.item)
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

    override fun setAdapter(adapter: ViewModelListAdapter) {
        uiWorker.execute {
            this.adapter = adapter
            adapter.onDataChanged(viewModels)
            adapter.setNavigator(this)
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
                onAdded(key).viewModel
            } else {
                if (existing.requiresUpdate) {
                    existing.item = key
                    builder.onUpdateViewModel(existing.container.viewModel as VM, key)
                    existing.requiresUpdate = false
                }
                existing.container.viewModel
            }
        }

        override fun indexOf(element: ViewModel): Int {
            return -1
        }

        override fun lastIndexOf(element: ViewModel): Int {
            return -1
        }

    }

}