package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.mvvm.viewmodel.ViewModel

interface StackNavigationModel {

    /**
     * Set stack to have single [ViewModel] without any animations.
     *
     * This is designed for setting initial state of [StackNavigationModel]
     */
    fun set(viewModel: ViewModel?) {
        if (viewModel != null) {
            replaceAll(viewModel, null)
        } else {
            clear(null)
        }
    }

    /**
     * Get number of [ViewModel]s in stack
     */
    fun getSize(): Int

    /**
     * Get list of [ViewModel] which now in stack
     */
    fun getStack(): List<ViewModel>

    /**
     * Get [ViewModel] at specified [index].
     *
     * @throws [IndexOutOfBoundsException] in case when requested index is out of stack
     */
    fun getItemAt(index: Int): ViewModel

    /**
     * Remove all [ViewModel]s from stack and set it to empty state
     */
    fun clear(transitionInfo: Any? = null)

    /**
     * Replace current [ViewModel] and all [ViewModel]s from stack by new [viewModel]
     */
    fun replaceAll(viewModel: ViewModel, transitionInfo: Any? = null)

    /**
     * Add [ViewModel] to stack
     */
    fun add(viewModel: ViewModel, transitionInfo: Any? = null)

    /**
     * Replace last [ViewModel] in stack
     */
    fun replace(viewModel: ViewModel, transitionInfo: Any? = null)

    /**
     * Remove specific [ViewModel] for stack
     * @param transitionInfo will be used only if this [ViewModel] is now active
     */
    fun remove(viewModel: ViewModel, transitionInfo: Any? = null)

    /**
     * Remove [ViewModel] at specific [index] of stack
     */
    fun removeAt(index: Int, transitionInfo: Any? = null) {
        remove(getItemAt(index), transitionInfo)
    }

    /**
     * Remove last [ViewModel] from stack and return back to previous. Will do nothing if stack is
     * empty
     * @return true if last model removed from stack
     */
    fun removeLast(transitionInfo: Any? = null): Boolean {
        return popBackStack(transitionInfo)
    }

    /**
     * Remove last [ViewModel] from stack and return back to previous. Will do nothing if stack is
     * empty or if current [ViewModel] is single in stack
     * @return true if last model removed from stack
     */
    fun popBackStack(transitionInfo: Any? = null): Boolean {
        return if (getSize() > 1) {
            remove(getItemAt(getSize() - 1), transitionInfo)
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
        if (!popBackStack(transitionInfo)) {
            actionIfEmpty()
        }
    }

    /**
     * Remove all [ViewModel]s from stack which are placed after specified [viewModel].
     * Will do nothing if stack is empty of [viewModel] is not present in stack
     * @return true if anything is removed from stack
     */
    fun popBackStackTo(viewModel: ViewModel, transitionInfo: Any? = null): Boolean

}