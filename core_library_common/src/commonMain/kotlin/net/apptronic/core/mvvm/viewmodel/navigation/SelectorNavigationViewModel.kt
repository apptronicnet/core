package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.component.context.Context
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.mvvm.viewmodel.adapter.SingleViewModelAdapter
import net.apptronic.core.mvvm.viewmodel.adapter.SingleViewModelListAdapter
import net.apptronic.core.mvvm.viewmodel.adapter.ViewModelListAdapter
import net.apptronic.core.mvvm.viewmodel.navigation.SelectorNavigationModel.Companion.SELECTOR_LAST
import net.apptronic.core.mvvm.viewmodel.navigation.SelectorNavigationModel.Companion.SELECTOR_NOTHING
import net.apptronic.core.mvvm.viewmodel.navigation.SelectorNavigationModel.Companion.SELECTOR_NOT_CHANGED
import kotlin.math.min

@UnderDevelopment
fun IViewModel.selectorNavigator(navigatorContext: Context = this.context): SelectorNavigationViewModel {
    context.verifyNavigatorContext(navigatorContext)
    return SelectorNavigationViewModel(context, navigatorContext)
}

@UnderDevelopment
class SelectorNavigationViewModel internal constructor(
        context: ViewModelContext, override val navigatorContext: Context
) : ViewModel(context), SelectorNavigationModel, SingleViewModelNavigationModel, SingleViewModelListNavigationModel {

    private var selectorIndexValue: Int = SELECTOR_NOTHING

    private val listNavigator = listNavigator(navigatorContext)

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

        fun refreshState(transitionSpec: Any?)

    }

    private inner class SelectorAdapter(val target: SingleViewModelAdapter) : TargetAdapter, ViewModelListAdapter {

        override fun onDataChanged(items: List<ViewModelListItem>, changeInfo: Any?) {
            refreshState(changeInfo)
        }

        var oldIndex = selectorIndexValue
        var oldViewModel: ViewModelListItem? = null

        override fun refreshState(transitionSpec: Any?) {
            val oldViewModelItem: ViewModelListItem? = this.oldViewModel
            val newViewModelItem: ViewModelListItem? = listNavigator.getViewModelItemAtOrNull(selectorIndexValue)
            if (oldViewModelItem != newViewModelItem) {
                val oldIndex = oldIndex
                val newIndex = selectorIndex
                if (oldViewModelItem != null) {
                    oldViewModelItem.setFocused(false)
                    oldViewModelItem.setVisible(false)
                    oldViewModelItem.setBound(false)
                }
                if (newViewModelItem != null) {
                    newViewModelItem.setBound(true)
                }
                val transitionInfo = TransitionInfo(newIndex > oldIndex, transitionSpec)
                target.onInvalidate(newViewModelItem?.viewModel, transitionInfo)
                if (newViewModelItem != null) {
                    newViewModelItem.setVisible(true)
                    newViewModelItem.setFocused(true)
                }
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

    private inner class SelectorListAdapter(val target: SingleViewModelListAdapter) : TargetAdapter, ViewModelListAdapter {

        override fun onDataChanged(items: List<ViewModelListItem>, changeInfo: Any?) {
            refreshState(changeInfo)
        }

        var oldIndex = selectorIndexValue

        override fun refreshState(transitionSpec: Any?) {
            val oldIndex = oldIndex
            val newIndex = selectorIndex
            val transitionInfo = TransitionInfo(newIndex > oldIndex, transitionSpec)
            target.onInvalidate(listNavigator.getViewModelItems(), newIndex, transitionInfo)
            this.oldIndex = newIndex
        }

    }

    override val size: Int
        get() = listNavigator.getSize()

    override val list: List<IViewModel>
        get() = listNavigator.get()

    override val selectorIndex: Int
        get() {
            return selectorIndexValue
        }

    override fun setSelectorIndex(index: Int, transitionInfo: Any?) {
        when {
            selectorIndexValue >= 0 -> selectorIndexValue = min(lastIndex, selectorIndex)
            selectorIndexValue == SELECTOR_NOTHING -> selectorIndexValue = SELECTOR_NOTHING
            selectorIndexValue == SELECTOR_NOT_CHANGED -> selectorIndexValue = min(lastIndex, selectorIndexValue)
            selectorIndexValue == SELECTOR_LAST -> selectorIndexValue = lastIndex
        }
        currentAdapter?.refreshState(transitionInfo)
    }

    override fun update(transitionInfo: Any?, selectorIndex: Int, builder: Context.(MutableList<IViewModel>) -> Unit) {
        val newList = mutableListOf<IViewModel>()
        newList.addAll(list)
        context.builder(newList)
        when {
            selectorIndexValue >= 0 -> selectorIndexValue = min(lastIndex, selectorIndex)
            selectorIndexValue == SELECTOR_NOTHING -> selectorIndexValue = SELECTOR_NOTHING
            selectorIndexValue == SELECTOR_NOT_CHANGED -> selectorIndexValue = min(newList.size - 1, selectorIndexValue)
            selectorIndexValue == SELECTOR_LAST -> selectorIndexValue = newList.size - 1
        }
        listNavigator.set(newList, transitionInfo)
    }

}