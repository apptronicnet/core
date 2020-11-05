package net.apptronic.test.commons_sample_app.lazylistfiltering

import net.apptronic.core.context.Context
import net.apptronic.core.entity.value
import net.apptronic.core.viewmodel.EmptyViewModelContext
import net.apptronic.core.viewmodel.ViewModel

class DynamicItemViewModel(
    parent: Context,
    text: String
) : ViewModel(parent, EmptyViewModelContext) {

    val text = value(text)

}