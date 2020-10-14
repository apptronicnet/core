package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.base.EntityValue
import net.apptronic.core.component.value
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.SingleViewModelAdapter

fun IViewModel.stackNavigator(navigatorContext: Context = this.context): StackNavigator {
    context.verifyNavigatorContext(navigatorContext)
    return StackNavigator(this, navigatorContext)
}

fun IViewModel.stackNavigator(source: Entity<IViewModel>, navigatorContext: Context = this.context): StackNavigator {
    context.verifyNavigatorContext(navigatorContext)
    return StackNavigator(this, navigatorContext).apply {
        source.subscribe(navigatorContext) {
            set(it)
        }
    }
}

class StackNavigator internal constructor(
        parent: IViewModel,
        override val navigatorContext: Context
) : Navigator<StackNavigatorStatus>(
        parent
), StackNavigationModel, SingleViewModelNavigationModel, VisibilityFilterableNavigator {

    private data class State(
            val isInProgress: Boolean,
            val visibleItem: ViewModelContainer?, // current item placed inside adapter
            val actualItem: ViewModelContainer? // current item which is last in stack
    )

    private var currentState = State(false, null, null)
    private val contentData = parent.value(
            StackNavigatorStatus(false, null, null, 0, emptyList())
    )
    private val stack = mutableListOf<ViewModelContainer>()
    private val removingItems = mutableListOf<ViewModelContainer>()
    private var currentAdapter: CurrentAdapter? = null
    private val visibilityFilters: VisibilityFilters<IViewModel> = VisibilityFilters<IViewModel>()


    private fun updateSubject() {
        val next = StackNavigatorStatus(
                isInProgress = currentState.isInProgress,
                visibleModel = currentState.visibleItem?.getViewModel(),
                actualModel = currentState.actualItem?.getViewModel(),
                size = getSize(),
                stack = stack.map { it.getViewModel() }
        )
        val current = contentData.get()
        if (!current.deepEquals(next)) {
            contentData.update(next)
        }
    }

    private class CurrentAdapter(
            val adapter: SingleViewModelAdapter
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

    override val content: EntityValue<StackNavigatorStatus> = contentData

    override fun getVisibilityFilters(): VisibilityFilters<IViewModel> {
        return visibilityFilters
    }

    override fun getStack(): List<IViewModel> {
        return stack.map { it.getViewModel() }
    }

    private class PendingTransition(
            val transitionInfo: Any?,
            val stackTransition: StackTransition
    )

    private var pendingTransition: PendingTransition? = null


    private fun refreshState(postTransition: Any?, stackTransition: StackTransition) {
        pendingTransition = PendingTransition(postTransition, stackTransition)
        refreshState()
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
                    invalidateAdapter(
                            newState.visibleItem,
                            pendingTransition?.transitionInfo,
                            pendingTransition?.stackTransition ?: StackTransition.Auto
                    )
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
            transitionInfo: Any?,
            stackTransition: StackTransition
    ) {
        val newItemIndex = newItem?.let { stack.indexOf(it) } ?: -1
        val isNewOnFront = when (stackTransition) {
            StackTransition.Auto -> newItemIndex >= currentItemIndex
            StackTransition.NewOnFront -> true
            StackTransition.NewOnBack -> false
        }
        currentItemIndex = newItemIndex
        currentAdapter?.apply {
            val oldItem = activeItem
            if (oldItem != null) {
                onUnbind(oldItem)
            }
            newItem?.setBound(true)
            adapter.onInvalidate(
                    newItem?.getViewModel(),
                    TransitionInfo(isNewOnFront, transitionInfo)
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
     * Set [SingleViewModelAdapter] to create view controllers for [ViewModel]s
     */
    override fun setAdapter(adapter: SingleViewModelAdapter) {
        currentAdapter = CurrentAdapter(adapter)
        invalidateAdapter(newItem = currentState.visibleItem, transitionInfo = null, stackTransition = StackTransition.Auto)
        context.lifecycle.onExitFromActiveStage {
            val currentItem = currentState.visibleItem
            if (currentItem != null) {
                onUnbind(currentItem)
            }
            currentAdapter = null
        }
    }

    override fun getItemAt(index: Int): IViewModel {
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
        clearAndSet(null, transitionInfo, StackTransition.Auto)
    }

    override fun replaceAll(viewModel: IViewModel, transitionInfo: Any?, stackTransition: StackTransition) {
        // to make transition for new item as for front
        currentItemIndex = -1
        clearAndSet(viewModel, transitionInfo, stackTransition)
    }

    private fun createStackItem(viewModel: IViewModel): ViewModelContainer {
        return ViewModelContainer(
                viewModel,
                parent,
                visibilityFilters.isReadyToShow(viewModel)
        )
    }

    private fun addStackItem(viewModel: IViewModel?) {
        if (viewModel != null) {
            val item = createStackItem(viewModel)
            stack.add(item)
            onAdded(item)
        }
    }

    private fun clearAndSet(viewModel: IViewModel?, transitionInfo: Any? = null, stackTransition: StackTransition) {
        viewModel?.verifyContext()
        removeFromStack(stack)
        addStackItem(viewModel)
        refreshState(transitionInfo, stackTransition)
    }

    override fun add(viewModel: IViewModel, transitionInfo: Any?, stackTransition: StackTransition) {
        viewModel.verifyContext()
        addStackItem(viewModel)
        refreshState(transitionInfo, stackTransition)
    }

    override fun replace(viewModel: IViewModel, transitionInfo: Any?, stackTransition: StackTransition) {
        viewModel.verifyContext()
        val actualItem = currentState.actualItem
        if (actualItem != null) {
            removeFromStack(actualItem)
        }
        addStackItem(viewModel)
        refreshState(transitionInfo, stackTransition)
    }

    override fun replaceStack(newStack: List<IViewModel>, transitionInfo: Any?, stackTransition: StackTransition) {
        newStack.forEach {
            it.verifyContext()
        }
        val newContainers = mutableListOf<ViewModelContainer>()
        val nextContainers = newStack.map { viewModel ->
            val existingContainer = stack.firstOrNull {
                it.getViewModel() == viewModel
            }
            if (existingContainer != null) {
                existingContainer
            } else {
                val newContainer = createStackItem(viewModel)
                newContainers.add(newContainer)
                newContainer
            }
        }
        val removingContainers = stack.filterNot {
            newStack.contains(it.getViewModel())
        }
        removingItems.addAll(removingContainers)
        stack.clear()
        stack.addAll(nextContainers)
        newContainers.forEach {
            onAdded(it)
        }
        refreshState(transitionInfo, stackTransition)
    }

    override fun remove(viewModel: IViewModel, transitionInfo: Any?, stackTransition: StackTransition) {
        val currentItem = stack.lastOrNull {
            it.getViewModel() == viewModel
        }
        if (currentItem != null) {
            removeFromStack(currentItem)
            if (currentItem.getViewModel() == viewModel) {
                refreshState(transitionInfo, stackTransition)
            } else {
                refreshState()
            }
        }
    }

    override fun popBackStackTo(viewModel: IViewModel, transitionInfo: Any?, stackTransition: StackTransition): Boolean {
        return if (stack.any { it.getViewModel() == viewModel } && stack.lastOrNull()?.getViewModel() != viewModel) {
            while (stack.isNotEmpty() && stack.lastOrNull()?.getViewModel() != viewModel) {
                removeFromStack(stack.last())
            }
            refreshState(transitionInfo, stackTransition)
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
        item.observeVisibilityChanged {
            refreshState()
        }
        item.setAttached(true)
    }

    private fun onRemoved(item: ViewModelContainer) {
        item.getViewModel().onDetachFromParent()
        item.terminate()
    }

    override fun requestCloseSelf(viewModel: IViewModel, transitionInfo: Any?) {
        remove(viewModel, transitionInfo)
    }

}