package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.value
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.mvvm.viewmodel.adapter.SingleViewModelAdapter
import net.apptronic.core.mvvm.viewmodel.adapter.SingleViewModelListAdapter
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter
import net.apptronic.core.mvvm.viewmodel.navigation.SelectorNavigationModel.Companion.SELECTOR_LAST
import net.apptronic.core.mvvm.viewmodel.navigation.SelectorNavigationModel.Companion.SELECTOR_NOTHING
import net.apptronic.core.mvvm.viewmodel.navigation.SelectorNavigationModel.Companion.SELECTOR_SAME_ITEM
import net.apptronic.core.mvvm.viewmodel.navigation.SelectorNavigationModel.Companion.SELECTOR_SAME_POSITION
import kotlin.math.max
import kotlin.math.min

@UnderDevelopment
fun IViewModel.selectorNavigator(navigatorContext: Context = this.context): SelectorNavigationViewModel {
    context.verifyNavigatorContext(navigatorContext)
    return SelectorNavigationViewModel(context, navigatorContext)
}

private class SelectorListNavigator(
        parent: IViewModel, navigatorContext: Context
) : StaticListNavigator<Int>(parent, navigatorContext, SelectorNavigationModel.SELECTOR_NOTHING) {

    override fun requestCloseSelf(viewModel: IViewModel, transitionInfo: Any?) {
        val list = getAll().toMutableList()
        val selectedIndex = getState()
        val visibleItem = getViewModelItemAtOrNull(selectedIndex)
        list.remove(viewModel)
        val next = if (visibleItem == null) {
            -1
        } else if (visibleItem.viewModel === viewModel) {
            max(selectedIndex, list.size - 1)
        } else {
            list.indexOf(visibleItem.viewModel)
        }
        set(list, next, transitionInfo)
    }

    override fun calculateFilteredState(all: List<IViewModel>, visible: List<IViewModel>, state: Int): Int {
        return state
    }

}

@UnderDevelopment
class SelectorNavigationViewModel internal constructor(
        context: ViewModelContext, override val navigatorContext: Context
) : ViewModel(context), SelectorNavigationModel, SingleViewModelNavigationModel, SingleViewModelListNavigationModel {

    private val contentData = value<IViewModel?>(null)

    override val content = contentData

    private val listNavigator = SelectorListNavigator(this, navigatorContext)

    private var currentAdapter: TargetAdapter? = null

    override fun setAdapter(adapter: SingleViewModelAdapter) {
        if (currentAdapter != null) {
            throw IllegalStateException("$this already have an adapter")
        }
        val realAdapter = SelectorAdapter(adapter)
        currentAdapter = realAdapter
        listNavigator.setAdapter(realAdapter)
        context.lifecycle.onExitFromActiveStage {
            currentAdapter = null
        }
    }

    private interface TargetAdapter {

        fun refreshState(nextIndex: Int, transitionSpec: Any?)

    }

    private inner class SelectorAdapter(val target: SingleViewModelAdapter) : TargetAdapter, ViewModelListAdapter<Int> {

        override fun onDataChanged(items: List<ViewModelItem>, state: Int, changeInfo: Any?) {
            refreshState(state, changeInfo)
        }

        var oldIndex = selectorIndex
        var oldViewModel: ViewModelItem? = null
        var targetInitialized: Boolean = false

        override fun refreshState(nextIndex: Int, transitionSpec: Any?) {
            val oldViewModelItem: ViewModelItem? = this.oldViewModel
            val newViewModelItem: ViewModelItem? = listNavigator.getViewModelItemAtOrNull(nextIndex)
            if (oldViewModelItem != newViewModelItem || !targetInitialized) {
                targetInitialized = true
                val oldIndex = oldIndex
                val newIndex = selectorIndex
                oldViewModelItem?.setFocused(false)
                oldViewModelItem?.setVisible(false)
                oldViewModelItem?.setBound(false)
                newViewModelItem?.setBound(true)
                val transitionInfo = TransitionInfo(newIndex > oldIndex, transitionSpec)
                target.onInvalidate(newViewModelItem?.viewModel, transitionInfo)
                newViewModelItem?.setVisible(true)
                newViewModelItem?.setFocused(true)
                this.oldIndex = newIndex
                this.oldViewModel = newViewModelItem
            }
        }

    }

    override fun setAdapter(adapter: SingleViewModelListAdapter) {
        if (currentAdapter != null) {
            throw IllegalStateException("$this already have an adapter")
        }
        val realAdapter = SelectorListAdapter(adapter)
        currentAdapter = realAdapter
        listNavigator.setAdapter(realAdapter)
        context.lifecycle.onExitFromActiveStage {
            currentAdapter = null
        }
    }

    private inner class SelectorListAdapter(val target: SingleViewModelListAdapter) : TargetAdapter, ViewModelListAdapter<Int> {

        override fun onDataChanged(items: List<ViewModelItem>, state: Int, changeInfo: Any?) {
            refreshState(state, changeInfo)
        }

        var oldIndex = selectorIndex

        override fun refreshState(nextIndex: Int, transitionSpec: Any?) {
            val oldIndex = oldIndex
            val newIndex = nextIndex
            val transitionInfo = TransitionInfo(newIndex > oldIndex, transitionSpec)
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
        listNavigator.setState(index, transitionSpec)
    }

    override fun update(transitionSpec: Any?, selectorIndex: Int, builder: Context.(MutableList<IViewModel>) -> Unit) {
        val newList = mutableListOf<IViewModel>()
        newList.addAll(list)
        context.builder(newList)
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