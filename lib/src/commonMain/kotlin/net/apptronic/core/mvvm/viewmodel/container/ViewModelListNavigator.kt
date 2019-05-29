package net.apptronic.core.mvvm.viewmodel.container

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.component.entity.UpdateEntity
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter
import net.apptronic.core.threading.execute

class ViewModelListNavigator(
        private val parent: ViewModel
) : Navigator<List<ViewModel>>(parent),
        UpdateEntity<List<ViewModel>>,
        ViewModelListAdapter.SourceNavigator {

    private val subject = BehaviorSubject<List<ViewModel>>()

    private fun updateSubject() {
        subject.update(items)
    }

    override fun update(value: List<ViewModel>) {
        set(value)
    }

    override fun getObservable(): Observable<List<ViewModel>> {
        return subject
    }

    private var adapter: ViewModelListAdapter? = null

    private var items: List<ViewModel> = emptyList()

    override fun get(): List<ViewModel> {
        return ArrayList(items)
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
        uiAsyncWorker.execute {
            val diff = getDiff(items, value)
            diff.removed.forEach {
                onRemoved(it)
            }
            items = value
            diff.added.forEach {
                onAdded(it)
            }
            adapter?.onDataChanged(items)
            updateSubject()
        }
    }

    private fun onAdded(viewModel: ViewModel) {
        val container = ViewModelContainerItem(viewModel, parent)
        containers[viewModel.getId()] = container
        container.viewModel.onAttachToParent(this)
        container.setCreated(true)
    }

    private fun onRemoved(viewModel: ViewModel) {
        viewModel.getContainer()?.let { container ->
            container.viewModel.onDetachFromParent()
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

    fun setAdapter(adapter: ViewModelListAdapter) {
        uiWorker.execute {
            this.adapter = adapter
            adapter.onDataChanged(items)
            adapter.setNavigator(this)
            parent.getLifecycle().onExitFromActiveStage {
                items.forEach {
                    setBound(it, false)
                }
                adapter.setNavigator(null)
                this.adapter = null
            }
        }
    }

}