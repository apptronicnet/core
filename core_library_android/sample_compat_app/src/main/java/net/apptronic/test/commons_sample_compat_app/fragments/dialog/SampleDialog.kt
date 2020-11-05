package net.apptronic.test.commons_sample_compat_app.fragments.dialog

import net.apptronic.core.android.compat.ViewBinderDialogFragment
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.context.Context

class SampleDialog : ViewBinderDialogFragment<DialogViewModel>() {

    override fun buildViewModel(parent: Context): DialogViewModel = parent.dialogViewModel()

    override fun buildViewBinder(): ViewBinder<DialogViewModel> = DialogViewBinder(this)

}