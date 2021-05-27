package net.apptronic.test.commons_sample_compat_app.fragments.enterdata.location

import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindClickListener
import net.apptronic.core.android.viewmodel.bindings.bindEnabledDisabled
import net.apptronic.core.android.viewmodel.bindings.bindTextInput
import net.apptronic.test.commons_sample_compat_app.R
import net.apptronic.test.commons_sample_compat_app.databinding.FragmentEnterLocationBinding

class EnterLocationBinder : ViewBinder<EnterLocationViewModel>() {

    override var layoutResId: Int? = R.layout.fragment_enter_location

    override fun onBindView() {
        withBinding(FragmentEnterLocationBinding::bind) {
            bindTextInput(edtCountry, viewModel.country)
            bindTextInput(edtCity, viewModel.city)
            bindEnabledDisabled(btnSubmit, viewModel.isSubmitEnabled)
            bindClickListener(btnSubmit, viewModel.onSubmitClick)
        }
    }

}