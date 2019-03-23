package net.apptronic.common.core.mvvm.viewmodel

import net.apptronic.common.core.component.ComponentContext
import net.apptronic.common.core.component.entity.base.DistinctUntilChangedStorePredicate
import net.apptronic.common.core.component.entity.entities.Property
import net.apptronic.common.core.mvvm.viewmodel.adapter.ViewModelAdapter
import java.util.*

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

    private val stack = LinkedList<ViewModel>()

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
        adapter?.apply {
            onInvalidate(
                oldModel = null,
                newModel = getOrNull(),
                transitionInfo = null
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
        adapter?.apply {
            onInvalidate(
                oldModel = activeModel,
                newModel = null,
                transitionInfo = transitionInfo
            )
        }
        updateFromGet()
    }

    /**
     * Add [ViewModel] to stack
     */
    fun add(viewModel: ViewModel, transitionInfo: Any? = null) {
        val activeModel = getOrNull()
        stack.add(viewModel)
        onAdded(viewModel)
        adapter?.apply {
            onInvalidate(
                oldModel = activeModel,
                newModel = viewModel,
                transitionInfo = transitionInfo
            )
        }
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
        adapter?.apply {
            onInvalidate(
                oldModel = activeModel,
                newModel = viewModel,
                transitionInfo = transitionInfo
            )
        }
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
            adapter?.apply {
                onInvalidate(
                    oldModel = viewModel,
                    newModel = newActiveModel,
                    transitionInfo = transitionInfo
                )
            }
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
     * Remove all [ViewModel]s from stack which are placed after specified [viewModel].
     * Will do nothing if stack is empty of [viewModel] is not present in stack
     * @return true if anything is removed from stack
     */
    fun popBackStackTo(viewModel: ViewModel, transitionInfo: Any? = null): Boolean {
        return if (stack.contains(viewModel) && stack.last != viewModel) {
            val activeModel = getOrNull()
            while (stack.last != viewModel) {
                stack.removeLast().apply {
                    finishLifecycle()
                    onRemoved(this)
                }
            }
            adapter?.apply {
                onInvalidate(
                    oldModel = activeModel,
                    newModel = viewModel,
                    transitionInfo = transitionInfo
                )
            }
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
    }

    private fun onRemoved(viewModel: ViewModel) {
        viewModel.onDetachFromParent()
    }

    override fun requestCloseSelf(viewModel: ViewModel, transitionInfo: Any?) {
        remove(viewModel, transitionInfo)
    }

}