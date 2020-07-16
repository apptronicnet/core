package net.apptronic.test.commons_sample_compat_app.fragments.enterdata.location

import net.apptronic.core.android.compat.ViewBinderFragment
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.component.context.Context

class EnterLocationFragment : ViewBinderFragment<EnterLocationViewModel>() {

    override fun buildViewModel(parent: Context): EnterLocationViewModel =
        parent.enterLocationViewModel()

    override fun buildViewBinder(): ViewBinder<EnterLocationViewModel> = EnterLocationBinder()

}