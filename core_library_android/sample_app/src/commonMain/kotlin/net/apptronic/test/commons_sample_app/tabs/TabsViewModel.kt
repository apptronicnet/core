package net.apptronic.test.commons_sample_app.tabs

import net.apptronic.core.context.Contextual
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.ViewModelContext
import net.apptronic.core.viewmodel.navigation.BasicTransition
import net.apptronic.core.viewmodel.navigation.selectorNavigator
import net.apptronic.core.viewmodel.viewModelContext

fun Contextual.tabsViewModel() = TabsViewModel(viewModelContext())

class TabsViewModel(context: ViewModelContext) : ViewModel(context) {

    val tabNavigator = selectorNavigator()

    init {
        tabNavigator.set(
            listOf(
                tabPageViewModel("Tab 1", 0xFFFF9080.toInt()),
                tabPageViewModel("Tab 2", 0xFFA8FFC8.toInt()),
                tabPageViewModel("Tab 3", 0xFF40FFFF.toInt()),
                tabPageViewModel("Tab 4", 0xFFD4C0FF.toInt())
            ), selectorIndex = 0
        )
    }

    fun selectTab(index: Int) {
        tabNavigator.setSelectorIndex(index, BasicTransition.Fade)
    }

}