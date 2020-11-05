package net.apptronic.core.viewmodel.navigation

import net.apptronic.core.context.Context
import net.apptronic.core.entity.commons.asProperty
import net.apptronic.core.entity.function.map
import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.navigation.adapters.SingleViewModelAdapter
import net.apptronic.core.viewmodel.navigation.adapters.SingleViewModelListAdapter
import net.apptronic.core.viewmodel.navigation.adapters.ViewModelListAdapter
import net.apptronic.core.viewmodel.navigation.models.ISelectorNavigationModel
import net.apptronic.core.viewmodel.navigation.models.ISelectorNavigationModel.Companion.SELECTOR_LAST
import net.apptronic.core.viewmodel.navigation.models.ISelectorNavigationModel.Companion.SELECTOR_NOTHING
import net.apptronic.core.viewmodel.navigation.models.ISelectorNavigationModel.Companion.SELECTOR_SAME_ITEM
import net.apptronic.core.viewmodel.navigation.models.ISelectorNavigationModel.Companion.SELECTOR_SAME_POSITION
import net.apptronic.core.viewmodel.navigation.models.SingleItemNavigatorContent
import kotlin.math.min

fun IViewModel.selectorNavigator(navigatorContext: Context = this.context): SelectorNavigationModel {
    context.verifyNavigatorContext(navigatorContext)
    return SelectorNavigationModel(this, navigatorContext)
}

class SelectorNavigationModel internal constructor(
        parent: IViewModel, override val navigatorContext: Context
) : Navigator<SingleItemNavigatorContent>(parent), ISelectorNavigationModel {

    private val listNavigator = SingleViewModelTargetStaticListNavigator(parent, navigatorContext)

    override val content = listNavigator.content.map {
        SingleItemNavigatorContent(it.state, it.all)
    }.asProperty()

    override fun requestCloseSelf(viewModel: IViewModel, transitionInfo: Any?) {
        listNavigator.requestCloseSelf(viewModel, transitionInfo)
    }

    override fun setAdapter(adapter: SingleViewModelAdapter) {
        listNavigator.setAdapter(SelectorAdapter(adapter))
    }

    private inner class SelectorAdapter(val target: SingleViewModelAdapter) : ViewModelListAdapter<Int> {

        private var oldViewModel: ViewModelItem? = null
        private var targetInitialized: Boolean = false
        private var lastIndex: Int = -1

        override fun onDataChanged(items: List<ViewModelItem>, state: Int, updateSpec: Any?) {
            val oldViewModelItem: ViewModelItem? = this.oldViewModel
            val newViewModelItem: ViewModelItem? = items.getOrNull(state)
            if (oldViewModelItem != newViewModelItem || !targetInitialized) {
                targetInitialized = true
                oldViewModelItem?.setFocused(false)
                oldViewModelItem?.setVisible(false)
                oldViewModelItem?.setBound(false)
                newViewModelItem?.setBound(true)
                target.onInvalidate(newViewModelItem, updateSpec.castAsTransitionInfo(state >= lastIndex))
                newViewModelItem?.setVisible(true)
                newViewModelItem?.setFocused(true)
                this.oldViewModel = newViewModelItem
                lastIndex = state
            }
        }

    }

    override fun setAdapter(adapter: SingleViewModelListAdapter) {
        listNavigator.setAdapter(SelectorListAdapter(adapter))
    }

    private inner class SelectorListAdapter(val target: SingleViewModelListAdapter) : ViewModelListAdapter<Int> {

        private var lastIndex: Int = -1

        override fun onDataChanged(items: List<ViewModelItem>, state: Int, updateSpec: Any?) {
            target.onInvalidate(items, state, updateSpec.castAsTransitionInfo(state >= lastIndex))
            lastIndex = state
        }

    }

    override fun onNavigated(index: Int) {
        set(items, index)
    }

    override fun setAdapter(adapter: ViewModelListAdapter<in Int>) {
        listNavigator.setAdapter(adapter)
    }

    override val size: Int
        get() = listNavigator.size

    override val items: List<IViewModel>
        get() = listNavigator.content.get().all

    override val selectorIndex: Int
        get() {
            return listNavigator.state
        }

    override fun setSelectorIndex(
            index: Int, transitionSpec: Any?,
            navigationTransition: NavigationTransition
    ) {
        val nextIndex = when {
            index >= 0 -> min(lastIndex, index)
            index == SELECTOR_NOTHING -> SELECTOR_NOTHING
            index == SELECTOR_SAME_POSITION -> min(lastIndex, selectorIndex)
            index == SELECTOR_SAME_ITEM -> min(lastIndex, selectorIndex)
            index == SELECTOR_LAST -> lastIndex
            else -> SELECTOR_NOTHING
        }
        val currentIndex = listNavigator.content.get().state
        val isNewOnFront = when (navigationTransition) {
            NavigationTransition.Auto -> nextIndex >= currentIndex
            NavigationTransition.NewOnFront -> true
            NavigationTransition.NewOnBack -> false
        }
        val transitionInfo = TransitionInfo(isNewOnFront, transitionSpec)
        listNavigator.updateState(nextIndex, transitionInfo)
    }

    override fun update(
            transitionSpec: Any?,
            selectorIndex: Int,
            navigationTransition: NavigationTransition,
            builder: Context.(MutableList<IViewModel>) -> Unit
    ) {
        val newList = mutableListOf<IViewModel>()
        newList.addAll(items)
        navigatorContext.builder(newList)
        val nextIndex = when {
            selectorIndex >= 0 -> min(newList.size - 1, selectorIndex)
            selectorIndex == SELECTOR_NOTHING -> SELECTOR_NOTHING
            selectorIndex == SELECTOR_SAME_POSITION -> min(newList.size - 1, this.selectorIndex)
            selectorIndex == SELECTOR_SAME_ITEM -> {
                val current = items.getOrNull(this.selectorIndex)
                newList.indexOf(current)
            }
            selectorIndex == SELECTOR_LAST -> newList.size - 1
            else -> SELECTOR_NOTHING
        }
        val currentIndex = listNavigator.content.get().state
        val isNewOnFront = when (navigationTransition) {
            NavigationTransition.Auto -> nextIndex >= currentIndex
            NavigationTransition.NewOnFront -> true
            NavigationTransition.NewOnBack -> false
        }
        val transitionInfo = TransitionInfo(isNewOnFront, transitionSpec)
        listNavigator.set(newList, nextIndex, transitionInfo)
    }

}