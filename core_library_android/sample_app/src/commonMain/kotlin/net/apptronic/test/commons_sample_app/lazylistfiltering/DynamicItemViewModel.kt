package net.apptronic.test.commons_sample_app.lazylistfiltering

import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.childContext
import net.apptronic.core.entity.commons.value
import net.apptronic.core.viewmodel.ViewModel

fun Contextual.dynamicItemViewModel(text: String) =
    DynamicItemViewModel(childContext(), text)

class DynamicItemViewModel(context: Context, text: String) : ViewModel(context) {

    val text = value(text)

}