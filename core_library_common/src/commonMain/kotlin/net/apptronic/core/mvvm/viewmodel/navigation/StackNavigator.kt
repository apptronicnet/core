package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelStackAdapter

class StackNavigator(
        parent: ViewModel
) : Navigator<StackNavigatorStatus>(
        parent
), StackNavigationModel, VisibilityFilterableNavigator {

    private data class State(
            val isInProgress: Boolean,
            val visibleItem: ViewModelContainer?, // current item placed inside adapter
            val actualItem: ViewModelContainer? // current item which is last in stack
    )

    private var currentState = State(false, null, null)
    override val subject = BehaviorSubject<StackNavigatorStatus>().apply {
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

    override fun get(): StackNavigatorStatus {
        return subject.getValue()!!.value
    }

    override fun getStack(): List<ViewModel> {
        return stack.map { it.getViewModel() }
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
        coroutineLauncher.launch {
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

    private var currentItemIndex: Int = -1

    private fun invalidateAdapter(
            newItem: ViewModelContainer?,
            transitionInfo: Any?
    ) {
        val newItemIndex = newItem?.let { stack.indexOf(it) } ?: -1
        val isNewOnFront = newItemIndex >= currentItemIndex
        currentItemIndex = newItemIndex
        currentAdapter?.apply {
            val oldItem = activeItem
            if (oldItem != null) {
                onUnbind(oldItem)
            }
            newItem?.setBound(true)
            adapter.onInvalidate(
                    newItem?.getViewModel(),
                    isNewOnFront,
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
    override fun getSize(): Int {
        return stack.size
    }

    /**
     * Set [ViewModelStackAdapter] to create view controllers for [ViewModel]s
     */
    fun setAdapter(adapter: ViewModelStackAdapter) {
        currentAdapter = CurrentAdapter(adapter)
        invalidateAdapter(newItem = currentState.visibleItem, transitionInfo = null)
        context.lifecycle.onExitFromActiveStage {
            val currentItem = currentState.visibleItem
            if (currentItem != null) {
                onUnbind(currentItem)
            }
            currentAdapter = null
        }
    }

    override fun getItemAt(index: Int): ViewModel {
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
    override fun clear(transitionInfo: Any?) {
        clearAndSet(null, transitionInfo)
    }

    override fun replaceAll(viewModel: ViewModel, transitionInfo: Any?) {
        // to make transition for new item as for front
        currentItemIndex = -1
        clearAndSet(viewModel, transitionInfo)
    }

    private fun addStackItem(viewModel: ViewModel?) {
        if (viewModel != null) {
            if (viewModel.context.parent != context) {
                throw IllegalArgumentException("$viewModel context should be direct child of Navigator context")
            }
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
        coroutineLauncher.launch {
            removeFromStack(stack)
            addStackItem(viewModel)
            postRefreshState(transitionInfo)
        }
    }

    override fun add(viewModel: ViewModel, transitionInfo: Any?) {
        addStackItem(viewModel)
        postRefreshState(transitionInfo)
    }

    override fun replace(viewModel: ViewModel, transitionInfo: Any?) {
        val actualItem = currentState.actualItem
        if (actualItem != null) {
            removeFromStack(actualItem)
        }
        addStackItem(viewModel)
        postRefreshState(transitionInfo)
    }

    override fun remove(viewModel: ViewModel, transitionInfo: Any?) {
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

    override fun popBackStackTo(viewModel: ViewModel, transitionInfo: Any?): Boolean {
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
        item.setAttached(true)
    }

    private fun onRemoved(item: ViewModelContainer) {
        item.getViewModel().onDetachFromParent()
        item.terminate()
    }

    override fun requestCloseSelf(viewModel: ViewModel, transitionInfo: Any?) {
        remove(viewModel, transitionInfo)
    }

}