package net.apptronic.test.commons_sample_app.tabs

import net.apptronic.core.context.Contextual
import net.apptronic.core.entity.commons.value
import net.apptronic.core.view.properties.CoreColor
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.ViewModelContext
import net.apptronic.core.viewmodel.navigation.listDynamicNavigator
import net.apptronic.core.viewmodel.viewModelContext

fun Contextual.tabPageViewModel(title: String, backgroundColor: CoreColor) =
    TabPageViewModel(viewModelContext(), title, backgroundColor)

class TabPageViewModel(
    context: ViewModelContext,
    title: String,
    val backgroundColor: CoreColor
) : ViewModel(context) {

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