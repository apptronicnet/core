package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.onchange.onChangeValue
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext

fun ViewModel.stackNavigationModel(navigatorContext: Context = this.context): StackNavigationViewModel {
    context.verifyNavigatorContext(navigatorContext)
    return StackNavigationViewModel(context, navigatorContext)
}

/**
 * This model allows to implement stack navigation using [ListNavigator]. It allows to handle user gestures to perform
 * back navigation and save views for items in back stack improving rendering performance by preventing destroying
 * and recreating views for back stack.
 *
 * Based on adapter implementation there may be no possibility to have interpretation for transitionInfo events.
 */
class StackNavigationViewModel internal constructor(
        context: ViewModelContext,
        override val navigatorContext: Context
) : ViewModel(context), StackNavigationModel {

    private val viewModels = onChangeValue<List<ViewModel>, Any>(emptyList())

    private fun updateInternal(transitionInfo: Any?, stackTransition: StackTransition, action: (MutableList<ViewModel>) -> Unit) {
        val current = viewModels.get().value
        val next = current.toTypedArray().toMutableList()
        action(next)
        updateInternal(transitionInfo, stackTransition, next)
    }

    private fun updateInternal(transitionInfo: Any?, stackTransition: StackTransition, next: List<ViewModel>) {
        val current = viewModels.get().value
        next.forEach {
            it.verifyContext()
        }
        val isNewOnFront = when (stackTransition) {
            StackTransition.Auto -> next.size >= current.size
            StackTransition.NewOnFront -> true
            StackTransition.NewOnBack -> false
        }
        viewModels.set(next, TransitionInfo(isNewOnFront, transitionInfo))
    }

    /**
     * Notify adapter that user is navigated to specific [index] of stack.
     *
     * This will clear stack after [index].
     */
    fun onNavigated(index: Int) {
        val next = viewModels.get().value.take(index + 1)
        updateInternal(null, StackTransition.Auto, next)
    }

    val listNavigator: BaseListNavigator<ViewModel> = listNavigatorOnChange(viewModels)

    private fun currentViewModel(): ViewModel? {
        return viewModels.get().value.getOrNull(getSize() - 1)
    }

    override fun replaceStack(newStack: List<ViewModel>, transitionInfo: Any?, stackTransition: StackTransition) {
        updateInternal(transitionInfo, stackTransition, newStack)
    }

    override fun add(viewModel: ViewModel, transitionInfo: Any?, stackTransition: StackTransition) {
        updateInternal(transitionInfo, stackTransition) {
            it.add(viewModel)
        }
    }

    override fun remove(viewModel: ViewModel, transitionInfo: Any?, stackTransition: StackTransition) {
        updateInternal(transitionInfo, stackTransition) {
            it.remove(viewModel)
        }
    }

    override fun clear(transitionInfo: Any?) {
        updateInternal(transitionInfo, StackTransition.Auto, emptyList())
    }

    override fun getItemAt(index: Int): ViewModel {
        return viewModels.get().value[index]
    }

    override fun getSize(): Int {
        return viewModels.get().value.size
    }

    override fun getStack(): List<ViewModel> {
        return mutableListOf<ViewModel>().apply {
            addAll(viewModels.get().value)
        }
    }

    override fun popBackStackTo(viewModel: ViewModel, transitionInfo: Any?, stackTransition: StackTransition): Boolean {
        val previous = getStack().toTypedArray().toMutableList()
        val index = previous.indexOf(viewModel)
        if (index < 0) {
            return false
        }
        val next = previous.subList(0, index + 1)
        updateInternal(transitionInfo, stackTransition, next)
        return previous.size != next.size
    }

    override fun replace(viewModel: ViewModel, transitionInfo: Any?, stackTransition: StackTransition) {
        viewModel.verifyContext()
        updateInternal(transitionInfo, stackTransition) {
            if (it.size > 0) {
                it.removeAt(it.size - 1)
            }
            it.add(viewModel)
        }
    }

    override fun replaceAll(viewModel: ViewModel, transitionInfo: Any?, stackTransition: StackTransition) {
        updateInternal(transitionInfo, stackTransition, listOf(viewModel))
    }

}