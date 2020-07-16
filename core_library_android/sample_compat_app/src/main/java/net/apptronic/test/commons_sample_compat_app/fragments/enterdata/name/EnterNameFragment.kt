package net.apptronic.test.commons_sample_compat_app.fragments.enterdata.name

import net.apptronic.core.android.compat.ViewBinderFragment
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.component.context.Context

class EnterNameFragment : ViewBinderFragment<EnterNameViewModel>() {

    override fun buildViewModel(parent: Context): EnterNameViewModel = parent.enterNameViewModel()

    override fun buildViewBinder(): ViewBinder<EnterNameViewModel> = EnterNameBinder()

}