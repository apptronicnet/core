package net.apptronic.core.viewmodel.navigation

import net.apptronic.core.context.Context
import net.apptronic.core.entity.base.EntityValue
import net.apptronic.core.entity.commons.asProperty
import net.apptronic.core.entity.function.map
import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.navigation.adapters.SingleViewModelAdapter
import net.apptronic.core.viewmodel.navigation.adapters.SingleViewModelListAdapter
import net.apptronic.core.viewmodel.navigation.adapters.ViewModelListAdapter
import net.apptronic.core.viewmodel.navigation.models.IStackNavigationModel
import net.apptronic.core.viewmodel.navigation.models.SingleItemNavigatorContent

fun IViewModel.stackNavigator(navigatorContext: Context = this.context): StackNavigationModel {
    context.verifyNavigatorContext(navigatorContext)
    return StackNavigationModel(this, navigatorContext)
}

/**
 * This model allows to implement stack navigation using [StaticListNavigator]. It allows to handle user gestures to perform
 * back navigation and save views for items in back stack improving rendering performance by preventing destroying
 * and recreating views for back stack.
 *
 * Based on adapter implementation there may be no possibility to have interpretation for transitionInfo events.
 */
class StackNavigationModel internal constructor(
        parent: IViewModel,
        override val navigatorContext: Context
) : Navigator<SingleItemNavigatorContent>(parent), IStackNavigationModel {

    private val listNavigator = StatelessStaticListNavigator(parent, navigatorContext)

    override val content: EntityValue<SingleItemNavigatorContent> = listNavigator.content.map {
        SingleItemNavigatorContent(it.all.lastIndex, it.all)
    }.asProperty()

    private fun updateInternal(transitionInfo: Any?, navigationTransition: NavigationTransition, action: (MutableList<IViewModel>) -> Unit) {
        val current = items
        val next = current.toTypedArray().toMutableList()
        action(next)
        updateInternal(transitionInfo, navigationTransition, next)
    }

    private fun updateInternal(transitionInfo: Any?, navigationTransition: NavigationTransition, next: List<IViewModel>) {
        val current = items
        next.forEach {
            it.verifyContext()
        }
        val isNewOnFront = when (navigationTransition) {
            NavigationTransition.Auto -> next.size >= current.size
            NavigationTransition.NewOnFront -> true
            NavigationTransition.NewOnBack -> false
        }
        listNavigator.set(next, TransitionInfo(isNewOnFront, transitionInfo))
    }

    override fun onNavigated(index: Int) {
        items.take(index + 1)
        replaceStack(items)
    }

    override fun setAdapter(adapter: SingleViewModelAdapter) {
        listNavigator.setAdapter(SelectorAdapter(adapter))
    }

    private inner class SelectorAdapter(val target: SingleViewModelAdapter) : ViewModelListAdapter<Unit> {

        var oldViewModel: ViewModelItem? = null
        var targetInitialized: Boolean = false
        private var lastIndex: Int = -1

        override fun onDataChanged(items: List<ViewModelItem>, state: Unit, updateSpec: Any?) {
            val oldViewModelItem: ViewModelItem? = this.oldViewModel
            val newViewModelItem: ViewModelItem? = items.lastOrNull()
            if (oldViewModelItem != newViewModelItem || !targetInitialized) {
                targetInitialized = true
                oldViewModelItem?.setFocused(false)
                oldViewModelItem?.setVisible(false)
                oldViewModelItem?.setBound(false)
                newViewModelItem?.setBound(true)
                target.onInvalidate(newViewModelItem, updateSpec.castAsTransitionInfo(items.lastIndex >= lastIndex))
                newViewModelItem?.setVisible(true)
                newViewModelItem?.setFocused(true)
                this.oldViewModel = newViewModelItem
                lastIndex = items.lastIndex
            }
        }

    }

    override fun setAdapter(adapter: SingleViewModelListAdapter) {
        listNavigator.setAdapter(SelectorListAdapter(adapter))
    }

    private inner class SelectorListAdapter(val target: SingleViewModelListAdapter) : ViewModelListAdapter<Unit> {

        private var lastIndex: Int = -1

        override fun onDataChanged(items: List<ViewModelItem>, state: Unit, updateSpec: Any?) {
            target.onInvalidate(items, items.lastIndex, updateSpec.castAsTransitionInfo(items.lastIndex >= lastIndex))
            lastIndex = items.lastIndex
        }

    }

    override fun setAdapter(adapter: ViewModelListAdapter<in Unit>) {
        listNavigator.setAdapter(adapter)
    }

    override fun requestCloseSelf(viewModel: IViewModel, transitionInfo: Any?) {
        listNavigator.requestCloseSelf(viewModel, transitionInfo)
    }

    override fun replaceStack(newStack: List<IViewModel>, transitionInfo: Any?, navigationTransition: NavigationTransition) {
        updateInternal(transitionInfo, navigationTransition, newStack)
    }

    override fun add(viewModel: IViewModel, transitionInfo: Any?, navigationTransition: NavigationTransition) {
        updateInternal(transitionInfo, navigationTransition) {
            it.add(viewModel)
        }
    }

    override fun remove(viewModel: IViewModel, transitionInfo: Any?, navigationTransition: NavigationTransition) {
        updateInternal(transitionInfo, navigationTransition) {
            it.remove(viewModel)
        }
    }

    override fun clear(transitionInfo: Any?) {
        updateInternal(transitionInfo, NavigationTransition.Auto, emptyList())
    }

    override fun popBackStackTo(viewModel: IViewModel, transitionInfo: Any?, navigationTransition: NavigationTransition): Boolean {
        val previous = items.toTypedArray().toMutableList()
        val index = previous.indexOf(viewModel)
        if (index < 0) {
            return false
        }
        val next = previous.subList(0, index + 1)
        updateInternal(transitionInfo, navigationTransition, next)
        return previous.size != next.size
    }

    override fun replace(viewModel: IViewModel, transitionInfo: Any?, navigationTransition: NavigationTransition) {
        viewModel.verifyContext()
        updateInternal(transitionInfo, navigationTransition) {
            if (it.size > 0) {
                it.removeAt(it.size - 1)
            }
            it.add(viewModel)
        }
    }

    override fun replaceAll(viewModel: IViewModel, transitionInfo: Any?, navigationTransition: NavigationTransition) {
        updateInternal(transitionInfo, navigationTransition, listOf(viewModel))
    }

}