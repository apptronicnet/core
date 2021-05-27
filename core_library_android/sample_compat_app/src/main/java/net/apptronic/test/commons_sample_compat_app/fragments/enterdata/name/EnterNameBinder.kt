package net.apptronic.test.commons_sample_compat_app.fragments.enterdata.name

import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindClickListener
import net.apptronic.core.android.viewmodel.bindings.bindEnabledDisabled
import net.apptronic.core.android.viewmodel.bindings.bindTextInput
import net.apptronic.test.commons_sample_compat_app.R
import net.apptronic.test.commons_sample_compat_app.databinding.FragmentEnterNameBinding

class EnterNameBinder : ViewBinder<EnterNameViewModel>() {

    override var layoutResId: Int? = R.layout.fragment_enter_name

    override fun onBindView() {
        withBinding(FragmentEnterNameBinding::bind) {
            bindTextInput(edtFirstName, viewModel.firstName)
            bindTextInput(edtLastName, viewModel.lastName)
            bindEnabledDisabled(btnContinue, viewModel.isContinueEnabled)
            bindClickListener(btnContinue, viewModel.onContinueClick)
        }
    }

}