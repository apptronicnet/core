package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.component.context.Context
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModel

interface StackNavigationModel : INavigator<StackNavigatorStatus> {

    /**
     * Set stack to have single [ViewModel] without any animations.
     *
     * This is designed for setting initial state of [StackNavigationModel]
     */
    fun set(viewModel: IViewModel?) {
        if (viewModel != null) {
            replaceAll(viewModel)
        } else {
            clear()
        }
    }

    /**
     * Set stack to have single [ViewModel] build from [builder] without any animations.
     *
     * This is designed for setting initial state of [StackNavigationModel]
     */
    fun set(builder: Context.() -> IViewModel) {
        set(navigatorContext.builder())
    }

    /**
     * Get number of [ViewModel]s in stack
     */
    fun getSize(): Int

    /**
     * Get list of [ViewModel] which now in stack
     */
    fun getStack(): List<IViewModel>

    /**
     * Get [ViewModel] at specified [index].
     *
     * @throws [IndexOutOfBoundsException] in case when requested index is out of stack
     */
    fun getItemAt(index: Int): IViewModel

    /**
     * Remove all [ViewModel]s from stack and set it to empty state
     */
    fun clear(transitionInfo: Any? = null)

    /**
     * Replace current [ViewModel] and all [ViewModel]s from stack by new [viewModel]
     */
    fun replaceAll(viewModel: IViewModel, transitionInfo: Any? = null, stackTransition: StackTransition = StackTransition.Auto)

    /**
     * Build [ViewModel] replace all [ViewModel]s from stack by it
     */
    fun replaceAll(transitionInfo: Any? = null, stackTransition: StackTransition = StackTransition.Auto, builder: Context.() -> IViewModel) {
        replaceAll(navigatorContext.builder(), transitionInfo, stackTransition)
    }

    /**
     * Add [ViewModel] to stack
     */
    fun add(viewModel: IViewModel, transitionInfo: Any? = null, stackTransition: StackTransition = StackTransition.Auto)

    /**
     * Build [ViewModel] and add it to stack
     */
    fun add(transitionInfo: Any? = null, stackTransition: StackTransition = StackTransition.Auto, builder: Context.() -> IViewModel) {
        add(navigatorContext.builder(), transitionInfo, stackTransition)
    }

    /**
     * Replace last [ViewModel] in stack
     */
    fun replace(viewModel: IViewModel, transitionInfo: Any? = null, stackTransition: StackTransition = StackTransition.Auto)

    /**
     * Replace whole stack with new stack. [newStack] can contain [ViewModel]s from current stack - that [ViewModel]s
     * will be retained inside navigator.
     */
    fun replaceStack(newStack: List<IViewModel>, transitionInfo: Any? = null, stackTransition: StackTransition = StackTransition.Auto)

    /**
     * Get current stack and update it by adding new [ViewModel]s or removing existing [ViewModel]s
     */
    fun updateStack(transitionInfo: Any? = null, stackTransition: StackTransition = StackTransition.Auto, update: (List<IViewModel>) -> List<IViewModel>) {
        val current = getStack()
        val next = update(current)
        replaceStack(next, transitionInfo, stackTransition)
    }

    /**
     * Build [ViewModel] replace last [ViewModel] in stack by it
     */
    fun replace(transitionInfo: Any? = null, stackTransition: StackTransition = StackTransition.Auto, builder: Context.() -> IViewModel) {
        replace(navigatorContext.builder(), transitionInfo, stackTransition)
    }

    /**
     * Remove specific [ViewModel] for stack
     * @param transitionInfo will be used only if this [ViewModel] is now active
     */
    fun remove(viewModel: IViewModel, transitionInfo: Any? = null, stackTransition: StackTransition = StackTransition.Auto)

    /**
     * Remove [ViewModel] at specific [index] of stack
     */
    fun removeAt(index: Int, transitionInfo: Any? = null, stackTransition: StackTransition = StackTransition.Auto) {
        if (index < 0 || index >= getSize()) {
            return
        }
        remove(getItemAt(index), transitionInfo, stackTransition)
    }

    /**
     * Remove last [ViewModel] from stack and return back to previous. Will do nothing if stack is
     * empty
     * @return true if last model removed from stack
     */
    fun removeLast(transitionInfo: Any? = null, stackTransition: StackTransition = StackTransition.Auto): Boolean {
        return if (getSize() > 0) {
            remove(getItemAt(getSize() - 1), transitionInfo, stackTransition)
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
    fun popBackStack(transitionInfo: Any? = null, stackTransition: StackTransition = StackTransition.Auto): Boolean {
        return if (getSize() > 1) {
            remove(getItemAt(getSize() - 1), transitionInfo, stackTransition)
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
        navigateBack(null, StackTransition.Auto, actionOnNonNavigate)
    }

    /**
     * Remove last [ViewModel] from stack or execute action if current model is
     * last or no model in stack
     */
    fun navigateBack(transitionInfo: Any?, stackTransition: StackTransition = StackTransition.Auto, actionOnNonNavigate: () -> Unit = {}) {
        if (!popBackStack(transitionInfo)) {
            actionOnNonNavigate()
        }
    }

    /**
     * Remove all [ViewModel]s from stack which are placed after specified [viewModel].
     * Will do nothing if stack is empty of [viewModel] is not present in stack
     * @return true if anything is removed from stack
     */
    fun popBackStackTo(viewModel: IViewModel, transitionInfo: Any? = null, stackTransition: StackTransition = StackTransition.Auto): Boolean

    /**
     * Remove all [ViewModel]s from stack which are placed after specified index.
     * Will do nothing if stack is empty of index is out of stack
     * @return true if anything is removed from stack
     */
    fun popBackStackTo(index: Int, transitionInfo: Any? = null, stackTransition: StackTransition = StackTransition.Auto): Boolean {
        if (index < 0 || index >= getSize()) {
            return false
        }
        return popBackStackTo(getItemAt(index), transitionInfo, stackTransition)
    }

}