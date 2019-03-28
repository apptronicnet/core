package net.apptronic.core.mvvm.viewmodel

import net.apptronic.core.component.context.ComponentContext
import net.apptronic.core.component.entity.base.DistinctUntilChangedStorePredicate
import net.apptronic.core.component.entity.entities.Property
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelAdapter

class ViewModelContainer(
    context: ComponentContext
) : Property<ViewModel?>(
    context,
    DistinctUntilChangedStorePredicate()
), ViewModelParent {

    override fun isSet(): Boolean {
        return true
    }

    override fun onSetValue(value: ViewModel?) {
        clear()
        if (value != null) {
            add(value)
        }
    }

    override fun onGetValue(): ViewModel? {
        return getActiveModel()
    }

    /**
     * Get currently active model in stack
     */
    fun getActiveModel(): ViewModel? {
        return stack.lastOrNull()
    }

    private var adapter: ViewModelAdapter? = null

    private val stack = mutableListOf<ViewModel>()

    /**
     * Get size of stack
     */
    fun getSize(): Int {
        return stack.size
    }

    /**
     * Set [ViewModelAdapter] to create view controllers for [ViewModel]s
     */
    fun setAdapter(adapter: ViewModelAdapter?) {
        this.adapter = adapter
        if (adapter != null) {
            invalidate(
                oldModel = null, newModel = getOrNull(), transitionInfo = null
            )
        } else {
            val currentModel = getOrNull()
            if (currentModel != null) {
                onUnbind(currentModel)
            }
        }
    }

    private fun onBind(viewModel: ViewModel) {
        viewModel.getLifecycleController().setBound(true)
        viewModel.getLifecycleController().setVisible(true)
        viewModel.getLifecycleController().setFocused(true)
    }

    private fun onUnbind(viewModel: ViewModel) {
        viewModel.getLifecycleController().setBound(false)
        viewModel.getLifecycleController().setVisible(false)
        viewModel.getLifecycleController().setFocused(false)
    }

    private fun invalidate(
        oldModel: ViewModel?,
        newModel: ViewModel?,
        transitionInfo: Any?
    ) {
        adapter?.apply {
            if (oldModel != null) {
                onUnbind(oldModel)
            }
            if (newModel != null) {
                onBind(newModel)
            }
            onInvalidate(
                oldModel,
                newModel,
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
        val activeModel = getOrNull()
        stack.forEach {
            it.finishLifecycle()
            onRemoved(it)
        }
        stack.clear()
        invalidate(
            oldModel = activeModel,
            newModel = null,
            transitionInfo = transitionInfo
        )
        updateFromGet()
    }

    /**
     * Add [ViewModel] to stack
     */
    fun add(viewModel: ViewModel, transitionInfo: Any? = null) {
        val activeModel = getOrNull()
        stack.add(viewModel)
        onAdded(viewModel)
        invalidate(
            oldModel = activeModel,
            newModel = viewModel,
            transitionInfo = transitionInfo
        )
        updateFromGet()
    }

    /**
     * Replace last [ViewModel] in stack
     */
    fun replace(viewModel: ViewModel, transitionInfo: Any? = null) {
        val activeModel = getOrNull()
        activeModel?.also {
            stack.remove(it)
            onRemoved(it)
            activeModel.finishLifecycle()
        }
        stack.add(viewModel)
        onAdded(viewModel)
        invalidate(
            oldModel = activeModel,
            newModel = viewModel,
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
        viewModel.finishLifecycle()
        stack.remove(viewModel)
        onRemoved(viewModel)
        if (viewModel == activeModel) {
            val newActiveModel = getOrNull()
            invalidate(
                oldModel = viewModel,
                newModel = newActiveModel,
                transitionInfo = transitionInfo
            )
        }
        updateFromGet()
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
        return if (stack.contains(viewModel) && stack.last() != viewModel) {
            val activeModel = getOrNull()
            while (stack.last() != viewModel) {
                stack.removeAt(stack.size - 1).apply {
                    finishLifecycle()
                    onRemoved(this)
                }
            }
            invalidate(
                oldModel = activeModel,
                newModel = viewModel,
                transitionInfo = transitionInfo
            )
            workingPredicate.update(getOrNull())
            true
        } else {
            false
        }
    }

    fun finishAll() {
        stack.forEach {
            it.finishLifecycle()
        }
    }

    private fun onAdded(viewModel: ViewModel) {
        viewModel.onAttachToParent(this)
        viewModel.getLifecycleController().setCreated(true)
    }

    private fun onRemoved(viewModel: ViewModel) {
        viewModel.onDetachFromParent()
        viewModel.getLifecycleController().setCreated(false)
        viewModel.terminateSelf()
    }

    override fun requestCloseSelf(viewModel: ViewModel, transitionInfo: Any?) {
        remove(viewModel, transitionInfo)
    }

}