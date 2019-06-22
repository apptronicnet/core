package net.apptronic.test.commons_sample_app.lazylistfiltering

import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext

class DynamicItemViewModel(
    context: ViewModelContext,
    text: String
) : ViewModel(context) {

    val text = value(text)

}