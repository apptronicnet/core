package net.apptronic.test.commons_sample_compat_app.fragments.dialog

import net.apptronic.core.context.Contextual
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.ViewModelContext
import net.apptronic.core.viewmodel.viewModelContext

fun Contextual.dialogViewModel() = DialogViewModel(viewModelContext())

class DialogViewModel(context: ViewModelContext) : ViewModel(context) {
}