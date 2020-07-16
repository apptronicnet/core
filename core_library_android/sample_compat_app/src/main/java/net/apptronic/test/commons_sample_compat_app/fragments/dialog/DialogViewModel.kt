package net.apptronic.test.commons_sample_compat_app.fragments.dialog

import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.context.viewModelContext
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext

fun Contextual.dialogViewModel() = DialogViewModel(viewModelContext())

class DialogViewModel(context: ViewModelContext) : ViewModel(context) {
}