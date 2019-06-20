package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.distinctUntilChanged
import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.base.observable.subscribe
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
    ) {
        fun toStatus(): StackNavigatorStatus {
            return StackNavigatorStatus(
                    isInProgress = isInProgress,
                    visibleModel = visibleItem?.getViewModel(),
                    actualModel = actualItem?.getViewModel()
            )
        }
    }

    private val subject = BehaviorSubject<State>().apply {
        update(State(false, null, null))
    }
    private val observable = BehaviorSubject<StackNavigatorStatus>()
    private val stack = mutableListOf<ViewModelContainer>()
    private var currentAdapter: CurrentAdapter? = null
    private val visibilityFilters: VisibilityFilters<ViewModel> = VisibilityFilters<ViewModel>()

    private class CurrentAdapter(
            val adapter: ViewModelStackAdapter
    ) {
        var activeItem: ViewModelContainer? = null
    }

    override fun getObservable(): Observable<StackNavigatorStatus> {
        return observable.distinctUntilChanged()
    }

    init {
        subject.subscribe {
            observable.update(it.toStatus())
        }
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
        return subject.getValue()!!.value.toStatus()
    }

    override fun getOrNull(): StackNavigatorStatus {
        return get()
    }

    private var pendingTransition: Any? = null

    private fun refreshState(postTransition: Any?) {
        pendingTransition = postTransition
        refreshState()
    }

    private fun refreshState() {
        val currentState = subject.getValue()!!.value
        val actualItem = stack.lastOrNull()
        val newState = State(
                isInProgress = actualItem != null && actualItem.shouldShow().not(),
                visibleItem = stack.lastOrNull {
                    it.shouldShow()
                },
                actualItem = actualItem
        )
        if (newState != currentState) {
            subject.update(newState)
            invalidateAdapter(newState.visibleItem, pendingTransition)
            pendingTransition = null
        }
    }

    private fun invalidateAdapter(
            newItem: ViewModelContainer?,
            transitionInfo: Any?
    ) {
        currentAdapter?.apply {
            val oldItem = activeItem
            if (oldItem != null) {
                oldItem.setFocused(false)
                oldItem.setVisible(false)
                oldItem.setBound(false)
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
            if (oldItem != null) {
                onUnbind(oldItem)
            }
            activeItem = newItem
        }
    }

    /**
     * Get currently active model in stack
     */
    private fun getActiveModel(): ViewModel? {
        return getCurrentItem()?.getViewModel()
    }

    /**
     * Get size of stack
     */
    fun getSize(): Int {
        return stack.size
    }

    private fun getCurrentItem(): ViewModelContainer? = stack.lastOrNull()

    /**
     * Set [ViewModelStackAdapter] to create view controllers for [ViewModel]s
     */
    fun setAdapter(adapter: ViewModelStackAdapter) {
        uiWorker.execute {
            currentAdapter = CurrentAdapter(adapter)
            invalidateAdapter(newItem = getCurrentItem(), transitionInfo = null)
            parent.getLifecycle().onExitFromActiveStage {
                val currentItem = getCurrentItem()
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

    private fun clearAndSet(viewModel: ViewModel?, transitionInfo: Any? = null) {
        uiAsyncWorker.execute {
            val activeModel = getCurrentItem()
            stack.forEach {
                it.terminate()
                onRemoved(it)
            }
            stack.clear()
            viewModel?.let {
                val item = ViewModelContainer(
                        it,
                        parent,
                        visibilityFilters.isReadyToShow(it),
                        this::refreshState
                )
                stack.add(item)
                onAdded(item)
                item
            }
            refreshState(transitionInfo)
        }
    }

    /**
     * Add [ViewModel] to stack
     */
    fun add(viewModel: ViewModel, transitionInfo: Any? = null) {
        uiAsyncWorker.execute {
            val activeModel = getCurrentItem()
            val newItem = ViewModelContainer(viewModel, parent)
            stack.add(newItem)
            onAdded(newItem)
            refreshState(transitionInfo)
        }
    }

    /**
     * Replace last [ViewModel] in stack
     */
    fun replace(viewModel: ViewModel, transitionInfo: Any? = null) {
        uiAsyncWorker.execute {
            val currentItem = getCurrentItem()
            currentItem?.also {
                stack.remove(it)
                onRemoved(it)
            }
            val newItem = ViewModelContainer(viewModel, parent)
            stack.add(newItem)
            onAdded(newItem)
            refreshState(transitionInfo)
        }
    }

    /**
     * Remove specific [ViewModel] for stack
     * @param transitionInfo will be used only if this [ViewModel] is now active
     */
    fun remove(viewModel: ViewModel, transitionInfo: Any? = null) {
        uiAsyncWorker.execute {
            val currentItem = stack.lastOrNull {
                it.getViewModel() == viewModel
            }
            if (currentItem != null) {
                stack.remove(currentItem)
                onRemoved(currentItem)
                if (currentItem.getViewModel() == viewModel) {
                    refreshState(transitionInfo)
                } else {
                    refreshState()
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
        val activeModel = getActiveModel()
        return if (activeModel != null) {
            remove(activeModel, transitionInfo)
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
                stack.removeAt(stack.size - 1).apply {
                    onRemoved(this)
                }
            }
            refreshState(transitionInfo)
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