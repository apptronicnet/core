package net.apptronic.test.commons_sample_app.tabs

import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.childContext
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.navigation.BasicTransition
import net.apptronic.core.viewmodel.navigation.selectorNavigator

fun Contextual.tabsViewModel() = TabsViewModel(childContext())

class TabsViewModel(context: Context) : ViewModel(context) {

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