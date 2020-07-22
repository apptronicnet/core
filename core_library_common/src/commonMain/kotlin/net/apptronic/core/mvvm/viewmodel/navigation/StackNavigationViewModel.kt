package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.value
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

    init {
        doOnUnbind {
            clearUnusedTransitions()
        }
    }

    private val viewModels = value<List<ViewModel>>(emptyList())

    private class Transition(
            val from: ViewModel?,
            val to: ViewModel?,
            val transitionInfo: Any
    )

    private val transitions = mutableListOf<Transition>()

    private fun addTransition(from: ViewModel?, to: ViewModel?, transitionInfo: Any?) {
        if (isBound() && from != to) {
            transitions.removeAll {
                it.from == from && it.to == to
            }
            if (transitionInfo != null) {
                transitions.add(Transition(from, to, transitionInfo))
            }
        }
    }

    private fun clearUnusedTransitions() {
        val list = mutableListOf<ViewModel?>().apply {
            add(null)
            addAll(viewModels.get())
        }
        transitions.removeAll {
            (list.contains(it.from) && list.contains(it.to)).not()
        }
    }

    private fun update(action: (List<ViewModel>) -> List<ViewModel>) {
        val current = viewModels.get()
        val next = action(current)
        next.forEach {
            it.verifyContext()
        }
        viewModels.set(next)
    }

    /**
     * Notify adapter that user is navigated to specific [index] of stack.
     *
     * This will clear stack after [index].
     */
    fun onNavigated(index: Int) {
        update {
            it.take(index + 1)
        }
    }

    val listNavigator: BaseListNavigator<ViewModel> = listNavigator(viewModels)

    private fun currentViewModel(): ViewModel? {
        return viewModels.get().getOrNull(getSize() - 1)
    }

    /**
     * Retrieve transitionInfo object for switching between [from] and [to] [ViewModel]s
     */
    fun getTransitionInfo(from: ViewModel?, to: ViewModel?): Any? {
        val transition = transitions.firstOrNull {
            it.from == from && it.to == to
        }
        if (transition != null) {
            transitions.remove(transition)
        }
        return transition?.transitionInfo
    }

    override fun add(viewModel: ViewModel, transitionInfo: Any?) {
        viewModel.verifyContext()
        addTransition(currentViewModel(), viewModel, transitionInfo)
        update { previous ->
            mutableListOf<ViewModel>().apply {
                addAll(previous)
                add(viewModel)
            }
        }
    }

    override fun remove(viewModel: ViewModel, transitionInfo: Any?) {
        val next = viewModels.get().filter {
            it != viewModel
        }
        addTransition(currentViewModel(), next.lastOrNull(), transitionInfo)
        update {
            next
        }
    }

    override fun clear(transitionInfo: Any?) {
        addTransition(currentViewModel(), null, transitionInfo)
        update {
            emptyList()
        }
    }

    override fun getItemAt(index: Int): ViewModel {
        return viewModels.get()[index]
    }

    override fun getSize(): Int {
        return viewModels.get().size
    }

    override fun getStack(): List<ViewModel> {
        return viewModels.get()
    }

    override fun popBackStackTo(viewModel: ViewModel, transitionInfo: Any?): Boolean {
        addTransition(currentViewModel(), viewModel, transitionInfo)
        val previous = getStack().toTypedArray().toMutableList()
        val index = previous.indexOf(viewModel)
        if (index < 0) {
            return false
        }
        val next = previous.subList(0, index + 1)
        update {
            next
        }
        return previous.size != next.size
    }

    override fun replace(viewModel: ViewModel, transitionInfo: Any?) {
        viewModel.verifyContext()
        addTransition(currentViewModel(), viewModel, transitionInfo)
        update { previous ->
            if (previous.isEmpty()) {
                listOf(viewModel)
            } else {
                previous.dropLast(1).toMutableList().also {
                    it.add(viewModel)
                }
            }
        }
    }

    override fun replaceAll(viewModel: ViewModel, transitionInfo: Any?) {
        viewModel.verifyContext()
        addTransition(currentViewModel(), viewModel, transitionInfo)
        update {
            listOf(viewModel)
        }
    }

}