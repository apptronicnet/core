package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelStackAdapter
import net.apptronic.core.threading.execute

class StackNavigator(
        private val parent: ViewModel
) : Navigator<StackNavigatorStatus>(
        parent
), VisibilityFilterableNavigator {

    private data class State(
            val isInProgress: Boolean,
            val visibleItem: ViewModelContainer?, // current item placed inside adapter
            val actualItem: ViewModelContainer? // current item which is last in stack
    )

    private var currentState = State(false, null, null)
    private val subject = BehaviorSubject<StackNavigatorStatus>().apply {
        update(StackNavigatorStatus(false, null, null, 0, emptyList()))
    }
    private val stack = mutableListOf<ViewModelContainer>()
    private val removingItems = mutableListOf<ViewModelContainer>()
    private var currentAdapter: CurrentAdapter? = null
    private val visibilityFilters: VisibilityFilters<ViewModel> = VisibilityFilters<ViewModel>()

    private fun updateSubject() {
        val next = StackNavigatorStatus(
                isInProgress = currentState.isInProgress,
                visibleModel = currentState.visibleItem?.getViewModel(),
                actualModel = currentState.actualItem?.getViewModel(),
                size = getSize(),
                stack = stack.map { it.getViewModel() }
        )
        val current = subject.getValue()?.value
        if (current == null || !current.deepEquals(next)) {
            subject.update(next)
        }
    }

    private class CurrentAdapter(
            val adapter: ViewModelStackAdapter
    ) {
        var activeItem: ViewModelContainer? = null
    }

    override fun getObservable(): Observable<StackNavigatorStatus> {
        return subject
    }

    private fun removeFromStack(item: ViewModelContainer) {
        if (stack.contains(item)) {
            removingItems.add(item)
            stack.remove(item)
        }
    }

    private fun removeFromStack(collection: Collection<ViewModelContainer>) {
        collection.toTypedArray().forEach {
            removeFromStack(it)
        }
    }

    init {
        parent.doOnTerminate {
            finishAll()
        }
    }

    override fun getVisibilityFilters(): VisibilityFilters<ViewModel> {
        return visibilityFilters
    }

    fun set(value: ViewModel?) {
        clearAndSet(value)
    }

    override fun get(): StackNavigatorStatus {
        return subject.getValue()!!.value
    }

    override fun getOrNull(): StackNavigatorStatus {
        return get()
    }

    private var pendingTransition: Any? = null

    private fun postRefreshState(postTransition: Any?) {
        pendingTransition = postTransition
        postRefreshState()
    }

    private fun postRefreshState() {
        uiAsyncWorker.execute {
            refreshState()
        }
    }

    private fun refreshState() {
        val oldItem = currentAdapter?.activeItem
        removePending()

        val oldState = currentState
        val actualItem = stack.lastOrNull()
        val visibleItem = if (actualItem != null) {
            if (actualItem.shouldShow()) {
                actualItem
            } else {
                oldItem
            }
        } else {
            null
        }
        val newState = State(
                isInProgress = actualItem != null && actualItem != visibleItem,
                visibleItem = visibleItem,
                actualItem = actualItem
        )
        if (newState != oldState) {
            currentState = newState
            currentAdapter?.let {
                val newItem = newState.visibleItem
                if (it.activeItem != newItem) {
                    invalidateAdapter(newState.visibleItem, pendingTransition)
                    pendingTransition = null
                }
            } ?: run {
                pendingTransition = null
            }
            removePending()
        }
        updateSubject()
    }

    private fun removePending() {
        val currentItemInAdapter = currentAdapter?.activeItem
        removingItems.removeAll {
            val remove = currentItemInAdapter == null || it != currentItemInAdapter
            if (remove) {
                onRemoved(it)
            }
            remove
        }
    }

    private fun invalidateAdapter(
            newItem: ViewModelContainer?,
            transitionInfo: Any?
    ) {
        currentAdapter?.apply {
            val oldItem = activeItem
            if (oldItem != null) {
                onUnbind(oldItem)
            }
            newItem?.setBound(true)
            adapter.onInvalidate(
                    oldItem?.getViewModel(),
                    newItem?.getViewModel(),
                    transitionInfo
            )
            if (newItem != null) {
                newItem.setVisible(true)
                newItem.setFocused(true)
            }
            activeItem = newItem
        }
    }

    /**
     * Get size of stack
     */
    fun getSize(): Int {
        return stack.size
    }

    /**
     * Set [ViewModelStackAdapter] to create view controllers for [ViewModel]s
     */
    fun setAdapter(adapter: ViewModelStackAdapter) {
        uiWorker.execute {
            currentAdapter = CurrentAdapter(adapter)
            invalidateAdapter(newItem = currentState.visibleItem, transitionInfo = null)
            parent.getLifecycle().onExitFromActiveStage {
                val currentItem = currentState.visibleItem
                if (currentItem != null) {
                    onUnbind(currentItem)
                }
                currentAdapter = null
            }
        }
    }

    fun getItemAt(index: Int): ViewModel {
        return stack[index].getViewModel()
    }

    private fun onUnbind(item: ViewModelContainer) {
        item.setBound(false)
        item.setVisible(false)
        item.setFocused(false)
    }

    /**
     * Clear all [ViewModel]s from stack
     */
    fun clear(transitionInfo: Any? = null) {
        clearAndSet(null, transitionInfo)
    }

    /**
     * Replace current [ViewModel] and all [ViewModel]s from stack by new [viewModel]
     */
    fun replaceAll(viewModel: ViewModel, transitionInfo: Any? = null) {
        clearAndSet(viewModel, transitionInfo)
    }

    private fun addStackItem(viewModel: ViewModel?) {
        if (viewModel != null) {
            val item = ViewModelContainer(
                    viewModel,
                    parent,
                    visibilityFilters.isReadyToShow(viewModel)
            )
            stack.add(item)
            onAdded(item)
        }
    }

    private fun clearAndSet(viewModel: ViewModel?, transitionInfo: Any? = null) {
        uiWorker.execute {
            removeFromStack(stack)
            addStackItem(viewModel)
            postRefreshState(transitionInfo)
        }
    }

    /**
     * Add [ViewModel] to stack
     */
    fun add(viewModel: ViewModel, transitionInfo: Any? = null) {
        uiWorker.execute {
            addStackItem(viewModel)
            postRefreshState(transitionInfo)
        }
    }

    /**
     * Replace last [ViewModel] in stack
     */
    fun replace(viewModel: ViewModel, transitionInfo: Any? = null) {
        uiWorker.execute {
            val actualItem = currentState.actualItem
            if (actualItem != null) {
                removeFromStack(actualItem)
            }
            addStackItem(viewModel)
            postRefreshState(transitionInfo)
        }
    }

    /**
     * Remove specific [ViewModel] for stack
     * @param transitionInfo will be used only if this [ViewModel] is now active
     */
    fun remove(viewModel: ViewModel, transitionInfo: Any? = null) {
        uiWorker.execute {
            val currentItem = stack.lastOrNull {
                it.getViewModel() == viewModel
            }
            if (currentItem != null) {
                removeFromStack(currentItem)
                if (currentItem.getViewModel() == viewModel) {
                    postRefreshState(transitionInfo)
                } else {
                    postRefreshState()
                }
            }
        }
    }

    /**
     * Remove last [ViewModel] from stack and return back to previous. Will do nothing if stack is
     * empty
     * @return true if last model removed from stack
     */
    fun popBackStack(transitionInfo: Any? = null): Boolean {
        val actualItem = currentState.actualItem
        return if (actualItem != null) {
            remove(actualItem.getViewModel(), transitionInfo)
            true
        } else {
            false
        }
    }

    /**
     * Remove last [ViewModel] from stack or execute action if current model is
     * last or no model in stack
     */
    fun navigateBack(actionIfEmpty: () -> Unit) {
        navigateBack(null, actionIfEmpty)
    }

    /**
     * Remove last [ViewModel] from stack or execute action if current model is
     * last or no model in stack
     */
    fun navigateBack(transitionInfo: Any?, actionIfEmpty: () -> Unit) {
        uiWorker.execute {
            if (stack.size > 1) {
                popBackStack(transitionInfo)
            } else {
                actionIfEmpty()
            }
        }
    }

    /**
     * Remove all [ViewModel]s from stack which are placed after specified [viewModel].
     * Will do nothing if stack is empty of [viewModel] is not present in stack
     * @return true if anything is removed from stack
     */
    fun popBackStackTo(viewModel: ViewModel, transitionInfo: Any? = null): Boolean {
        return if (stack.any { it.getViewModel() == viewModel } && stack.lastOrNull()?.getViewModel() != viewModel) {
            while (stack.isNotEmpty() && stack.lastOrNull()?.getViewModel() != viewModel) {
                removeFromStack(stack.last())
            }
            postRefreshState(transitionInfo)
            true
        } else {
            false
        }
    }

    private fun finishAll() {
        stack.forEach {
            it.terminate()
        }
    }

    private fun onAdded(item: ViewModelContainer) {
        item.getViewModel().onAttachToParent(this)
        item.observeVisibilityChanged(::postRefreshState)
        item.setCreated(true)
    }

    private fun onRemoved(item: ViewModelContainer) {
        item.getViewModel().onDetachFromParent()
        item.terminate()
    }

    override fun requestCloseSelf(viewModel: ViewModel, transitionInfo: Any?) {
        remove(viewModel, transitionInfo)
    }

}