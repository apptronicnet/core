package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.component.context.Context
import net.apptronic.core.mvvm.viewmodel.ViewModel

interface StackNavigationModel : INavigator {

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
     * Set stack to have single [ViewModel] build from [builder] without any animations.
     *
     * This is designed for setting initial state of [StackNavigationModel]
     */
    fun set(builder: Context.() -> ViewModel) {
        set(navigatorContext.builder())
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
     * Build [ViewModel] replace all [ViewModel]s from stack by it
     */
    fun replaceAll(transitionInfo: Any? = null, builder: Context.() -> ViewModel) {
        replaceAll(navigatorContext.builder(), transitionInfo)
    }

    /**
     * Add [ViewModel] to stack
     */
    fun add(viewModel: ViewModel, transitionInfo: Any? = null)

    /**
     * Build [ViewModel] and add it to stack
     */
    fun add(transitionInfo: Any? = null, builder: Context.() -> ViewModel) {
        add(navigatorContext.builder(), transitionInfo)
    }

    /**
     * Replace last [ViewModel] in stack
     */
    fun replace(viewModel: ViewModel, transitionInfo: Any? = null)

    /**
     * Build [ViewModel] replace last [ViewModel] in stack by it
     */
    fun replace(transitionInfo: Any? = null, builder: Context.() -> ViewModel) {
        replace(navigatorContext.builder(), transitionInfo)
    }

    /**
     * Remove specific [ViewModel] for stack
     * @param transitionInfo will be used only if this [ViewModel] is now active
     */
    fun remove(viewModel: ViewModel, transitionInfo: Any? = null)

    /**
     * Remove [ViewModel] at specific [index] of stack
     */
    fun removeAt(index: Int, transitionInfo: Any? = null) {
        if (index < 0 || index >= getSize()) {
            return
        }
        remove(getItemAt(index), transitionInfo)
    }

    /**
     * Remove last [ViewModel] from stack and return back to previous. Will do nothing if stack is
     * empty
     * @return true if last model removed from stack
     */
    fun removeLast(transitionInfo: Any? = null): Boolean {
        return if (getSize() > 0) {
            remove(getItemAt(getSize() - 1), transitionInfo)
            true
        } else {
            false
        }
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
    fun navigateBack(actionOnNonNavigate: () -> Unit = {}) {
        navigateBack(null, actionOnNonNavigate)
    }

    /**
     * Remove last [ViewModel] from stack or execute action if current model is
     * last or no model in stack
     */
    fun navigateBack(transitionInfo: Any?, actionOnNonNavigate: () -> Unit = {}) {
        if (!popBackStack(transitionInfo)) {
            actionOnNonNavigate()
        }
    }

    /**
     * Remove all [ViewModel]s from stack which are placed after specified [viewModel].
     * Will do nothing if stack is empty of [viewModel] is not present in stack
     * @return true if anything is removed from stack
     */
    fun popBackStackTo(viewModel: ViewModel, transitionInfo: Any? = null): Boolean

    /**
     * Remove all [ViewModel]s from stack which are placed after specified index.
     * Will do nothing if stack is empty of index is out of stack
     * @return true if anything is removed from stack
     */
    fun popBackStackTo(index: Int, transitionInfo: Any? = null): Boolean {
        if (index < 0 || index >= getSize()) {
            return false
        }
        return popBackStackTo(getItemAt(index), transitionInfo)
    }

}