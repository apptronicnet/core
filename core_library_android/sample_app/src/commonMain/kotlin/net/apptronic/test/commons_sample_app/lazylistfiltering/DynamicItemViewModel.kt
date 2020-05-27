package net.apptronic.test.commons_sample_app.lazylistfiltering

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.value
import net.apptronic.core.mvvm.viewmodel.EmptyViewModelContext
import net.apptronic.core.mvvm.viewmodel.ViewModel

class DynamicItemViewModel(
    parent: Context,
    text: String
) : ViewModel(parent, EmptyViewModelContext) {

    val text = value(text)

}