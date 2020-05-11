package net.apptronic.test.commons_sample_app.lazylistfiltering

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.value
import net.apptronic.core.mvvm.viewmodel.EMPTY_VIEW_MODEL_CONTEXT
import net.apptronic.core.mvvm.viewmodel.ViewModel

class DynamicItemViewModel(
    parent: Context,
    text: String
) : ViewModel(parent, EMPTY_VIEW_MODEL_CONTEXT) {

    val text = value(text)

}