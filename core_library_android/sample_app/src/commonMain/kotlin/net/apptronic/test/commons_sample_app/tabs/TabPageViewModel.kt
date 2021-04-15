package net.apptronic.test.commons_sample_app.tabs

import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.childContext
import net.apptronic.core.entity.commons.value
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.navigation.listDynamicNavigator

fun Contextual.tabPageViewModel(title: String, backgroundColor: Int) =
    TabPageViewModel(childContext(), title, backgroundColor)

class TabPageViewModel(context: Context, title: String, val backgroundColor: Int) :
    ViewModel(context) {

    val title = value(title)

    val listNavigator = listDynamicNavigator(TabListItemViewModelBuilder)

    init {
        listNavigator.setItems(
            (1..100).map {
                "Item #$it"
            }
        )
    }


}