package net.apptronic.test.commons_sample_app.tabs

import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.entity.value
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.ViewModelContext
import net.apptronic.core.viewmodel.navigation.ViewModelBuilder
import net.apptronic.core.viewmodel.viewModelContext

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