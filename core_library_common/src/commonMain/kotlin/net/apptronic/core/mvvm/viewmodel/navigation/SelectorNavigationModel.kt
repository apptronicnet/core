package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.value
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.adapter.SingleViewModelAdapter
import net.apptronic.core.mvvm.viewmodel.adapter.SingleViewModelListAdapter
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter
import net.apptronic.core.mvvm.viewmodel.navigation.ISelectorNavigationModel.Companion.SELECTOR_LAST
import net.apptronic.core.mvvm.viewmodel.navigation.ISelectorNavigationModel.Companion.SELECTOR_NOTHING
import net.apptronic.core.mvvm.viewmodel.navigation.ISelectorNavigationModel.Companion.SELECTOR_SAME_ITEM
import net.apptronic.core.mvvm.viewmodel.navigation.ISelectorNavigationModel.Companion.SELECTOR_SAME_POSITION
import kotlin.math.min

@UnderDevelopment
fun IViewModel.selectorNavigator(navigatorContext: Context = this.context): SelectorNavigationModel {
    context.verifyNavigatorContext(navigatorContext)
    return SelectorNavigationModel(this, navigatorContext)
}

@UnderDevelopment
class SelectorNavigationModel internal constructor(
        parent: IViewModel, override val navigatorContext: Context
) : Navigator<IViewModel?>(parent), ISelectorNavigationModel, SingleViewModelNavigationModel, SingleViewModelListNavigationModel {

    private val contentData = parent.value<IViewModel?>(null)

    override val content = contentData

    private val listNavigator = SingleViewModelTargetStaticListNavigator(parent, navigatorContext)

    override fun requestCloseSelf(viewModel: IViewModel, transitionInfo: Any?) {
        listNavigator.requestCloseSelf(viewModel, transitionInfo)
    }

    override fun setAdapter(adapter: SingleViewModelAdapter) {
        listNavigator.setAdapter(SelectorAdapter(adapter))
    }

    private inner class SelectorAdapter(val target: SingleViewModelAdapter) : ViewModelListAdapter<Int> {

        var oldIndex = selectorIndex
        var oldViewModel: ViewModelItem? = null
        var targetInitialized: Boolean = false

        override fun onDataChanged(items: List<ViewModelItem>, state: Int, changeInfo: Any?) {
            val oldViewModelItem: ViewModelItem? = this.oldViewModel
            val newViewModelItem: ViewModelItem? = items.getOrNull(state)
            if (oldViewModelItem != newViewModelItem || !targetInitialized) {
                targetInitialized = true
                val oldIndex = oldIndex
                val newIndex = selectorIndex
                oldViewModelItem?.setFocused(false)
                oldViewModelItem?.setVisible(false)
                oldViewModelItem?.setBound(false)
                newViewModelItem?.setBound(true)
                val transitionInfo = TransitionInfo(newIndex > oldIndex, changeInfo)
                target.onInvalidate(newViewModelItem, transitionInfo)
                newViewModelItem?.setVisible(true)
                newViewModelItem?.setFocused(true)
                this.oldIndex = newIndex
                this.oldViewModel = newViewModelItem
            }
        }

    }

    override fun setAdapter(adapter: SingleViewModelListAdapter) {
        listNavigator.setAdapter(SelectorListAdapter(adapter))
    }

    private inner class SelectorListAdapter(val target: SingleViewModelListAdapter) : ViewModelListAdapter<Int> {

        var oldIndex = selectorIndex

        override fun onDataChanged(items: List<ViewModelItem>, state: Int, changeInfo: Any?) {
            val oldIndex = oldIndex
            val newIndex = state
            val transitionInfo = TransitionInfo(newIndex > oldIndex, changeInfo)
            target.onInvalidate(listNavigator.getViewModelItems(), newIndex, transitionInfo)
            this.oldIndex = newIndex
        }

    }

    override val size: Int
        get() = listNavigator.getSize()

    override val list: List<IViewModel>
        get() = listNavigator.content.get().all

    override val selectorIndex: Int
        get() {
            return listNavigator.getState()
        }

    override fun setSelectorIndex(index: Int, transitionSpec: Any?) {
        val nextIndex = when {
            index >= 0 -> min(lastIndex, index)
            index == SELECTOR_NOTHING -> SELECTOR_NOTHING
            index == SELECTOR_SAME_POSITION -> min(lastIndex, selectorIndex)
            index == SELECTOR_SAME_ITEM -> min(lastIndex, selectorIndex)
            index == SELECTOR_LAST -> lastIndex
            else -> SELECTOR_NOTHING
        }
        listNavigator.setState(nextIndex, transitionSpec)
    }

    override fun update(transitionSpec: Any?, selectorIndex: Int, builder: Context.(MutableList<IViewModel>) -> Unit) {
        val newList = mutableListOf<IViewModel>()
        newList.addAll(list)
        navigatorContext.builder(newList)
        val nextIndex = when {
            selectorIndex >= 0 -> min(newList.size - 1, selectorIndex)
            selectorIndex == SELECTOR_NOTHING -> SELECTOR_NOTHING
            selectorIndex == SELECTOR_SAME_POSITION -> min(newList.size - 1, this.selectorIndex)
            selectorIndex == SELECTOR_SAME_ITEM -> {
                val current = list.getOrNull(this.selectorIndex)
                newList.indexOf(current)
            }
            selectorIndex == SELECTOR_LAST -> newList.size - 1
            else -> SELECTOR_NOTHING
        }
        listNavigator.set(newList, nextIndex, transitionSpec)
    }

}