package net.apptronic.test.commons_sample_app.tabs

import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.childContext
import net.apptronic.core.entity.commons.value
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.navigation.ViewModelAdapter

object TabListItemViewModelBuilder : ViewModelAdapter<String, String, TabListItemViewModel> {

    override fun getItemId(item: String): String = item

    override fun createViewModel(parent: Contextual, item: String): TabListItemViewModel {
        return parent.tabListItemViewModel(item)
    }

}

fun Contextual.tabListItemViewModel(text: String) = TabListItemViewModel(childContext(), text)

class TabListItemViewModel(context: Context, text: String) : ViewModel(context) {

    val text = value(text)

}