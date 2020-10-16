package net.apptronic.test.commons_sample_app.tabs

import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.context.viewModelContext
import net.apptronic.core.component.value
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.mvvm.viewmodel.navigation.listDynamicNavigator
import net.apptronic.core.view.properties.CoreColor

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