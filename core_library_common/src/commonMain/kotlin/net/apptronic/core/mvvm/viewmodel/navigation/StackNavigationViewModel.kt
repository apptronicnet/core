package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.component.property
import net.apptronic.core.component.value
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import kotlin.math.max

fun ViewModel.stackNavigationModel(): StackNavigationViewModel {
    return StackNavigationViewModel(context)
}

/**
 * This model allows to implement stack navigation using [ListNavigator]. It allows to handle user gestures to perform
 * back navigation and save views for items in back stack improving rendering performance by preventing destroying
 * and recreating views for back stack.
 *
 * Based on adapter implementation there may be no possibility to have interpretation for transitionInfo events.
 */
class StackNavigationViewModel internal constructor(context: ViewModelContext) : ViewModel(context), StackNavigationModel {

    private val viewModels = value<List<ViewModel>>(emptyList())

    private class Transition(
            val from: ViewModel?,
            val to: ViewModel?,
            val transitionInfo: Any
    )

    private val transitions = mutableListOf<Transition>()

    private fun addTransition(from: ViewModel?, to: ViewModel?, transitionInfo: Any?) {
        transitions.removeAll {
            it.from == from && it.to == to
        }
        if (transitionInfo != null) {
            transitions.add(Transition(from, to, transitionInfo))
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
        viewModels.set(next)
        clearUnusedTransitions()
        if (currentItemValue.get() >= getSize()) {
            currentItemValue.set(getSize() - 1)
        }
    }

    private val currentItemValue = value(-1)

    /**
     * Notify adapter that user is navigated to specific [index] of stack.
     *
     * This will clear stack after [index].
     */
    fun onNavigated(index: Int) {
        currentItemValue.set(index)
        update {
            it.take(index + 1)
        }
    }

    val listNavigator: BaseListNavigator<*> = listNavigator(viewModels)
    val currentItem = property(currentItemValue)

    private fun currentViewModel(): ViewModel? {
        return viewModels.get().getOrNull(currentItem.get())
    }

    /**
     * Retrieve transitionInfo object for switching between [from] and [to] [ViewModel]s
     */
    fun getTransitionInfo(from: ViewModel?, to: ViewModel?): Any? {
        return transitions.firstOrNull {
            it.from == from && it.to == to
        }?.transitionInfo
    }

    override fun add(viewModel: ViewModel, transitionInfo: Any?) {
        addTransition(currentViewModel(), viewModel, transitionInfo)
        update { previous ->
            mutableListOf<ViewModel>().apply {
                addAll(previous)
                add(viewModel)
            }
        }
    }

    override fun remove(viewModel: ViewModel, transitionInfo: Any?) {
        addTransition(currentViewModel(), viewModel, transitionInfo)
        update { previous ->
            previous.filter {
                it != viewModel
            }
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
        val next = previous.subList(0, max(0, index))
        update {
            next
        }
        return previous.size != next.size
    }

    override fun replace(viewModel: ViewModel, transitionInfo: Any?) {
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
        addTransition(currentViewModel(), viewModel, transitionInfo)
        update {
            listOf(viewModel)
        }
    }

}