package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.component.context.Context
import net.apptronic.core.mvvm.viewmodel.IViewModel
import kotlin.math.max

internal class SingleViewModelTargetStaticListNavigator(
        parent: IViewModel, navigatorContext: Context
) : StaticListNavigator<Int>(parent, navigatorContext, ISelectorNavigationModel.SELECTOR_NOTHING) {

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

