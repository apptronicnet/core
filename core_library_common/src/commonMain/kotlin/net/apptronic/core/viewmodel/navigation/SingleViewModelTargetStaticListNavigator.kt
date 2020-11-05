package net.apptronic.core.viewmodel.navigation

import net.apptronic.core.context.Context
import net.apptronic.core.viewmodel.IViewModel
import kotlin.math.max

internal class SingleViewModelTargetStaticListNavigator(
        parent: IViewModel, navigatorContext: Context
) : StaticListNavigator<Int>(parent, navigatorContext, -1) {

    override fun requestCloseSelf(viewModel: IViewModel, transitionInfo: Any?) {
        val list = getAll().toMutableList()
        val selectedIndex = state
        val visibleViewModel = getItems().getOrNull(selectedIndex)
        list.remove(viewModel)
        val next = if (visibleViewModel == null) {
            -1
        } else if (visibleViewModel === viewModel) {
            max(selectedIndex, list.size - 1)
        } else {
            list.indexOf(visibleViewModel)
        }
        set(list, next, transitionInfo)
    }

    override fun calculateFilteredState(all: List<IViewModel>, visible: List<IViewModel>, state: Int): Int {
        return state
    }

}

