package net.apptronic.core.viewmodel.navigation.models

import net.apptronic.core.context.Contextual
import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.navigation.NavigationTransition

interface IStackNavigationModel : ISingleNavigationModel, SupportsViewModelListAdapter<Unit> {

    /**
     * Set stack to have single [ViewModel] without any animations.
     *
     * This is designed for setting initial state of [IStackNavigationModel]
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
     * This is designed for setting initial state of [IStackNavigationModel]
     */
    fun set(builder: Contextual.() -> IViewModel) {
        set(navigatorContext.builder())
    }

    /**
     * Remove all [ViewModel]s from stack and set it to empty state
     */
    fun clear(transitionInfo: Any? = null)

    /**
     * Replace current [ViewModel] and all [ViewModel]s from stack by new [viewModel]
     */
    fun replaceAll(
        viewModel: IViewModel,
        transitionInfo: Any? = null,
        navigationTransition: NavigationTransition = NavigationTransition.NewOnFront
    )

    /**
     * Build [ViewModel] replace all [ViewModel]s from stack by it
     */
    fun replaceAll(
        transitionInfo: Any? = null,
        navigationTransition: NavigationTransition = NavigationTransition.Auto,
        builder: Contextual.() -> IViewModel
    ) {
        replaceAll(navigatorContext.builder(), transitionInfo, navigationTransition)
    }

    /**
     * Add [ViewModel] to stack
     */
    fun add(
        viewModel: IViewModel,
        transitionInfo: Any? = null,
        navigationTransition: NavigationTransition = NavigationTransition.Auto
    )

    /**
     * Build [ViewModel] and add it to stack
     */
    fun add(
        transitionInfo: Any? = null,
        navigationTransition: NavigationTransition = NavigationTransition.Auto,
        builder: Contextual.() -> IViewModel
    ) {
        add(navigatorContext.builder(), transitionInfo, navigationTransition)
    }

    /**
     * Replace last [ViewModel] in stack
     */
    fun replace(
        viewModel: IViewModel,
        transitionInfo: Any? = null,
        navigationTransition: NavigationTransition = NavigationTransition.Auto
    )

    /**
     * Build [ViewModel] replace last [ViewModel] in stack by it
     */
    fun replace(
        transitionInfo: Any? = null,
        navigationTransition: NavigationTransition = NavigationTransition.Auto,
        builder: Contextual.() -> IViewModel
    ) {
        replace(navigatorContext.builder(), transitionInfo, navigationTransition)
    }

    /**
     * Replace whole stack with new stack. [newStack] can contain [ViewModel]s from current stack - that [ViewModel]s
     * will be retained inside navigator.
     */
    fun replaceStack(
        newStack: List<IViewModel>,
        transitionInfo: Any? = null,
        navigationTransition: NavigationTransition = NavigationTransition.Auto
    )

    /**
     * Get current stack and update it by adding new [ViewModel]s or removing existing [ViewModel]s
     */
    fun updateStack(
        transitionInfo: Any? = null,
        navigationTransition: NavigationTransition = NavigationTransition.Auto,
        update: (List<IViewModel>) -> List<IViewModel>
    ) {
        val current = items
        val next = update(current)
        replaceStack(next, transitionInfo, navigationTransition)
    }

    /**
     * Remove specific [ViewModel] for stack
     * @param transitionInfo will be used only if this [ViewModel] is now active
     */
    fun remove(
        viewModel: IViewModel,
        transitionInfo: Any? = null,
        navigationTransition: NavigationTransition = NavigationTransition.Auto
    )

    /**
     * Remove [ViewModel] at specific [index] of stack
     */
    fun removeAt(
        index: Int,
        transitionInfo: Any? = null,
        navigationTransition: NavigationTransition = NavigationTransition.Auto
    ) {
        val viewModel = items.getOrNull(index)
        if (viewModel != null) {
            remove(viewModel, transitionInfo, navigationTransition)
        }
    }

    /**
     * Remove last [ViewModel] from stack and return back to previous. Will do nothing if stack is
     * empty
     * @return true if last model removed from stack
     */
    fun removeLast(
        transitionInfo: Any? = null,
        navigationTransition: NavigationTransition = NavigationTransition.Auto
    ): Boolean {
        val viewModel = items.lastOrNull()
        return if (viewModel != null) {
            remove(viewModel, transitionInfo, navigationTransition)
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
    fun popBackStack(
        transitionInfo: Any? = null,
        navigationTransition: NavigationTransition = NavigationTransition.Auto
    ): Boolean {
        return if (size > 1) {
            removeAt(lastIndex, transitionInfo, navigationTransition)
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
        navigateBack(null, NavigationTransition.Auto, actionOnNonNavigate)
    }

    /**
     * Remove last [ViewModel] from stack or execute action if current model is
     * last or no model in stack
     */
    fun navigateBack(
        transitionInfo: Any?,
        navigationTransition: NavigationTransition = NavigationTransition.Auto,
        actionOnNonNavigate: () -> Unit = {}
    ) {
        if (!popBackStack(transitionInfo)) {
            actionOnNonNavigate()
        }
    }

    /**
     * Remove all [ViewModel]s from stack which are placed after specified [viewModel].
     * Will do nothing if stack is empty of [viewModel] is not present in stack
     * @return true if anything is removed from stack
     */
    fun popBackStackTo(
        viewModel: IViewModel,
        transitionInfo: Any? = null,
        navigationTransition: NavigationTransition = NavigationTransition.Auto
    ): Boolean

    /**
     * Remove all [ViewModel]s from stack which are placed after specified index.
     * Will do nothing if stack is empty of index is out of stack
     * @return true if anything is removed from stack
     */
    fun popBackStackTo(
        index: Int,
        transitionInfo: Any? = null,
        navigationTransition: NavigationTransition = NavigationTransition.Auto
    ): Boolean {
        val viewModel = items.getOrNull(index)
        return if (viewModel != null) {
            popBackStackTo(viewModel, transitionInfo, navigationTransition)
        } else false
    }

}