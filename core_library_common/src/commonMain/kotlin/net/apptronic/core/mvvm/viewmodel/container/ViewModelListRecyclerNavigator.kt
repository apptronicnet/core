package net.apptronic.core.mvvm.viewmodel.container

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.component.entity.UpdateEntity
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter
import net.apptronic.core.threading.execute

class ViewModelListRecyclerNavigator<T, Id, VM : ViewModel>(
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
    private val viewModels = LazyList()

    override fun get(): List<T> {
        return ArrayList(items)
    }

    override fun getOrNull(): List<T>? {
        return ArrayList(items)
    }

    private inner class IdContainer(
        var item: T,
        val container: ViewModelContainerItem
    ) {
        var requiresUpdate = false
    }

    private val containers = mutableMapOf<Id, IdContainer>()

    private fun ViewModel.getContainer(): IdContainer? {
        return containers.values.firstOrNull {
            it.container.viewModel == this
        }
    }

    fun update(action: (MutableList<T>) -> Unit) {
        uiWorker.execute {
            val list = items.toMutableList()
            action.invoke(list)
            set(list)
        }
    }

    fun set(value: List<T>) {
        uiAsyncWorker.execute {
            containers.values.forEach {
                it.requiresUpdate = true
            }
            items = value
            adapter?.onDataChanged(viewModels)
            updateSubject()
        }
    }

    private fun T.getId(): Id {
        return builder.getId(this)
    }

    private fun onAdded(key: T): ViewModelContainerItem {
        val viewModel = builder.onCreateViewModel(parent, key)
        val container = ViewModelContainerItem(viewModel, parent)
        containers[key.getId()] = IdContainer(key, container)
        container.viewModel.onAttachToParent(this)
        container.setCreated(true)
        return container
    }

    private fun onRemoved(key: T) {
        containers[key.getId()]?.let { container ->
            container.container.viewModel.onDetachFromParent()
            container.container.terminate()
            containers.remove(key.getId())
        }
    }

    override fun requestCloseSelf(
        viewModel: ViewModel,
        transitionInfo: Any?
    ) {
        update {
            containers.forEach { entry ->
                if (entry.value.container.viewModel == viewModel) {
                    it.remove(entry.value.item)
                }
            }
        }
    }

    override fun setBound(viewModel: ViewModel, isBound: Boolean) {
        uiWorker.execute {
            if (isBound) {
                viewModel.getContainer()?.container?.setBound(isBound)
            } else {
                containers.entries.firstOrNull {
                    it.value.container.viewModel == viewModel
                }?.let {
                    val key = builder.getId(it.value.item)
                    if (builder.shouldRetainInstance(items, it.value.item, viewModel as VM)) {
                        viewModel.getContainer()?.container?.setBound(false)
                    } else {
                        onRemoved(it.value.item)
                    }
                }
            }
        }
    }

    override fun setVisible(viewModel: ViewModel, isBound: Boolean) {
        uiWorker.execute {
            viewModel.getContainer()?.container?.setVisible(isBound)
        }
    }

    override fun setFocused(viewModel: ViewModel, isBound: Boolean) {
        uiWorker.execute {
            viewModel.getContainer()?.container?.setFocused(isBound)
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
                containers.values.toTypedArray().forEach {
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
            val existing = containers[key.getId()]
            return if (existing == null) {
                onAdded(key).viewModel
            } else {
                if (existing.requiresUpdate) {
                    existing.item = key
                    builder.onUpdateViewModel(existing.container.viewModel as VM, key)
                }
                existing.container.viewModel
            }
        }

        override fun indexOf(element: ViewModel): Int {
            val entry = containers.entries.firstOrNull {
                it.value.container.viewModel == element
            }
            return if (entry != null) items.indexOf(entry.value.item) else -1
        }

        override fun lastIndexOf(element: ViewModel): Int {
            val entry = containers.entries.firstOrNull {
                it.value.container.viewModel == element
            }
            return if (entry != null) items.lastIndexOf(entry.value.item) else -1
        }

    }

}