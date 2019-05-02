package net.apptronic.core.mvvm.viewmodel.container

import net.apptronic.core.component.entity.base.DistinctUntilChangedStorePredicate
import net.apptronic.core.component.entity.entities.Property
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelParent
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelStackAdapter

class ViewModelStackNavigator(
    private val parent: ViewModel
) : Property<ViewModel?>(
    parent,
    DistinctUntilChangedStorePredicate()
), ViewModelParent {

    init {
        parent.doOnTerminate {
            finishAll()
        }
    }

    override fun isSet(): Boolean {
        return true
    }

    override fun onSetValue(value: ViewModel?) {
        clearAndSet(value)
    }

    override fun onGetValue(): ViewModel? {
        return getActiveModel()
    }

    /**
     * Get currently active model in stack
     */
    fun getActiveModel(): ViewModel? {
        return getCurrentItem()?.viewModel
    }

    private var adapter: ViewModelStackAdapter? = null

    private val stack = mutableListOf<ViewModelContainerItem>()

    /**
     * Get size of stack
     */
    fun getSize(): Int {
        return stack.size
    }

    private fun getCurrentItem(): ViewModelContainerItem? = stack.lastOrNull()

    /**
     * Set [ViewModelStackAdapter] to create view controllers for [ViewModel]s
     */
    fun setAdapter(adapter: ViewModelStackAdapter) {
        val currentItem = getCurrentItem()
        this.adapter = adapter
        invalidate(
            oldItem = null, newItem = getCurrentItem(), transitionInfo = null
        )
        parent.getLifecycle().onExitFromActiveStage {
            if (currentItem != null) {
                onUnbind(currentItem)
            }
            this.adapter = null
        }
    }

    fun getItemAt(index: Int): ViewModel {
        return stack[index].viewModel
    }

    private fun onBind(item: ViewModelContainerItem) {
        item.setBound(true)
        item.setVisible(true)
        item.setFocused(true)
    }

    private fun onUnbind(item: ViewModelContainerItem) {
        item.setBound(false)
        item.setVisible(false)
        item.setFocused(false)
    }

    private fun invalidate(
        oldItem: ViewModelContainerItem?,
        newItem: ViewModelContainerItem?,
        transitionInfo: Any?
    ) {
        adapter?.apply {
            if (oldItem != null) {
                onUnbind(oldItem)
            }
            if (newItem != null) {
                onBind(newItem)
            }
            onInvalidate(
                oldItem?.viewModel,
                newItem?.viewModel,
                transitionInfo
            )
        }
    }

    private fun updateFromGet() {
        workingPredicate.update(getOrNull())
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
        val activeModel = getCurrentItem()
        stack.forEach {
            it.terminate()
            onRemoved(it)
        }
        stack.clear()
        val newItem = viewModel?.let {
            val item = ViewModelContainerItem(it, parent)
            stack.add(item)
            onAdded(item)
            item
        }
        invalidate(
            oldItem = activeModel,
            newItem = newItem,
            transitionInfo = transitionInfo
        )
        updateFromGet()
    }

    /**
     * Add [ViewModel] to stack
     */
    fun add(viewModel: ViewModel, transitionInfo: Any? = null) {
        val activeModel = getCurrentItem()
        val newItem = ViewModelContainerItem(viewModel, parent)
        stack.add(newItem)
        onAdded(newItem)
        invalidate(
            oldItem = activeModel,
            newItem = newItem,
            transitionInfo = transitionInfo
        )
        updateFromGet()
    }

    /**
     * Replace last [ViewModel] in stack
     */
    fun replace(viewModel: ViewModel, transitionInfo: Any? = null) {
        val currentItem = getCurrentItem()
        currentItem?.also {
            stack.remove(it)
            onRemoved(it)
        }
        val newItem = ViewModelContainerItem(viewModel, parent)
        stack.add(newItem)
        onAdded(newItem)
        invalidate(
            oldItem = currentItem,
            newItem = newItem,
            transitionInfo = transitionInfo
        )
        updateFromGet()
    }

    /**
     * Remove specific [ViewModel] for stack
     * @param transitionInfo will be used only if this [ViewModel] is now active
     */
    fun remove(viewModel: ViewModel, transitionInfo: Any? = null) {
        val activeModel = getOrNull()
        val currentBox = stack.lastOrNull {
            it.viewModel == viewModel
        }
        if (currentBox != null) {
            stack.remove(currentBox)
            onRemoved(currentBox)
            if (viewModel == activeModel) {
                val newActiveBox = getCurrentItem()
                invalidate(
                    oldItem = currentBox,
                    newItem = newActiveBox,
                    transitionInfo = transitionInfo
                )
            }
            updateFromGet()
        }
    }

    /**
     * Remove last [ViewModel] from stack and return back to previous. Will do nothing if stack is
     * empty
     * @return true if last model removed from stack
     */
    fun popBackStack(transitionInfo: Any? = null): Boolean {
        val activeModel = getOrNull()
        return if (activeModel != null) {
            remove(activeModel, transitionInfo)
            workingPredicate.update(getOrNull())
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
        if (stack.size > 1) {
            popBackStack(transitionInfo)
        } else {
            actionIfEmpty()
        }
    }

    /**
     * Remove all [ViewModel]s from stack which are placed after specified [viewModel].
     * Will do nothing if stack is empty of [viewModel] is not present in stack
     * @return true if anything is removed from stack
     */
    fun popBackStackTo(viewModel: ViewModel, transitionInfo: Any? = null): Boolean {
        return if (stack.any { it.viewModel == viewModel } && stack.lastOrNull()?.viewModel != viewModel) {
            val activeBeforePop = getCurrentItem()
            while (stack.lastOrNull()?.viewModel != viewModel) {
                stack.removeAt(stack.size - 1).apply {
                    onRemoved(this)
                }
            }
            invalidate(
                oldItem = activeBeforePop,
                newItem = getCurrentItem(),
                transitionInfo = transitionInfo
            )
            workingPredicate.update(getOrNull())
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

    private fun onAdded(item: ViewModelContainerItem) {
        item.viewModel.onAttachToParent(this)
        item.setCreated(true)
    }

    private fun onRemoved(item: ViewModelContainerItem) {
        item.viewModel.onDetachFromParent()
        item.terminate()
    }

    override fun requestCloseSelf(viewModel: ViewModel, transitionInfo: Any?) {
        remove(viewModel, transitionInfo)
    }

}