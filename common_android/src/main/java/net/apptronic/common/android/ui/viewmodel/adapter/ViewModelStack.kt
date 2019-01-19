package net.apptronic.common.android.ui.viewmodel.adapter

import net.apptronic.common.android.ui.viewmodel.ViewModel
import java.util.*

class ViewModelStack {

    private var adapter: ViewModelAdapter? = null

    private val stack = LinkedList<ViewModel>()

    /**
     * Get currently active model in stack
     */
    fun getActiveModel(): ViewModel? {
        return stack.firstOrNull()
    }

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
                newModel = getActiveModel(),
                transitionInfo = null
            )
        }
    }

    /**
     * Clear all [ViewModel]s from stack
     */
    fun clear(transitionInfo: Any? = null) {
        val activeModel = getActiveModel()
        stack.forEach {
            it.finishLifecycle()
        }
        stack.clear()
        adapter?.apply {
            onInvalidate(
                oldModel = activeModel,
                newModel = null,
                transitionInfo = transitionInfo
            )
        }
    }

    /**
     * Add [ViewModel] to stack
     */
    fun add(viewModel: ViewModel, transitionInfo: Any? = null) {
        viewModel.startLifecycle()
        val activeModel = getActiveModel()
        stack.add(viewModel)
        viewModel.startLifecycle()
        adapter?.apply {
            onInvalidate(
                oldModel = activeModel,
                newModel = viewModel,
                transitionInfo = transitionInfo
            )
        }
    }

    /**
     * Replace last [ViewModel] in stack
     */
    fun replace(viewModel: ViewModel, transitionInfo: Any? = null) {
        viewModel.startLifecycle()
        val activeModel = getActiveModel()
        activeModel?.also {
            stack.remove(it)
            activeModel.finishLifecycle()
        }
        stack.add(viewModel)
        adapter?.apply {
            onInvalidate(
                oldModel = activeModel,
                newModel = viewModel,
                transitionInfo = transitionInfo
            )
        }
    }

    /**
     * Remove specific [ViewModel] for stack
     * @param transitionInfo will be used only if this [ViewModel] is now active
     */
    fun remove(viewModel: ViewModel, transitionInfo: Any? = null) {
        val activeModel = getActiveModel()
        viewModel.finishLifecycle()
        stack.remove(viewModel)
        if (viewModel == activeModel) {
            val newActiveModel = getActiveModel()
            adapter?.apply {
                onInvalidate(
                    oldModel = viewModel,
                    newModel = newActiveModel,
                    transitionInfo = transitionInfo
                )
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
     * Remove all [ViewModel]s from stack which are placed after specified [viewModel].
     * Will do nothing if stack is empty of [viewModel] is not present in stack
     * @return true if anything is removed from stack
     */
    fun popBackStackTo(viewModel: ViewModel, transitionInfo: Any? = null): Boolean {
        return if (stack.contains(viewModel) && stack.last != viewModel) {
            val activeModel = getActiveModel()
            while (stack.last != viewModel) {
                stack.removeLast().finishLifecycle()
            }
            adapter?.apply {
                onInvalidate(
                    oldModel = activeModel,
                    newModel = viewModel,
                    transitionInfo = transitionInfo
                )
            }
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

}