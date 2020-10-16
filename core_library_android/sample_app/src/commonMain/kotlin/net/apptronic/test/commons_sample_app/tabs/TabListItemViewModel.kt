package net.apptronic.test.commons_sample_app.tabs

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.context.viewModelContext
import net.apptronic.core.component.value
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.mvvm.viewmodel.navigation.ViewModelBuilder

object TabListItemViewModelBuilder : ViewModelBuilder<String, String, TabListItemViewModel> {

    override fun getId(item: String): String = item

    override fun onCreateViewModel(parent: Context, item: String): TabListItemViewModel {
        return parent.tabListItemViewModel(item)
    }

}

fun Contextual.tabListItemViewModel(text: String) = TabListItemViewModel(viewModelContext(), text)

class TabListItemViewModel(context: ViewModelContext, text: String) : ViewModel(context) {

    val text = value(text)

}