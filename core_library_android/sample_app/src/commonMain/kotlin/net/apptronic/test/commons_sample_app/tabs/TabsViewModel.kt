package net.apptronic.test.commons_sample_app.tabs

import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.context.viewModelContext
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.mvvm.viewmodel.navigation.BasicTransition
import net.apptronic.core.mvvm.viewmodel.navigation.selectorNavigator
import net.apptronic.core.view.properties.CoreColor

fun Contextual.tabsViewModel() = TabsViewModel(viewModelContext())

class TabsViewModel(context: ViewModelContext) : ViewModel(context) {

    val tabNavigator = selectorNavigator()

    init {
        tabNavigator.set(
            listOf(
                tabPageViewModel("Tab 1", CoreColor.rgbHex(0xFF9080)),
                tabPageViewModel("Tab 2", CoreColor.rgbHex(0xA8FFC8)),
                tabPageViewModel("Tab 3", CoreColor.rgbHex(0x40FFFF)),
                tabPageViewModel("Tab 4", CoreColor.rgbHex(0xD4C0FF))
            ), selectorIndex = 0
        )
    }

    fun selectTab(index: Int) {
        tabNavigator.setSelectorIndex(index, BasicTransition.Fade)
    }

}