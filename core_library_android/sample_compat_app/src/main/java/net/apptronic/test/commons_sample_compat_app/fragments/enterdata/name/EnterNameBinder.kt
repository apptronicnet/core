package net.apptronic.test.commons_sample_compat_app.fragments.enterdata.name

import android.view.View
import kotlinx.android.synthetic.main.fragment_enter_name.view.*
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindClickListener
import net.apptronic.core.android.viewmodel.bindings.bindEnabledDisabled
import net.apptronic.core.android.viewmodel.bindings.bindTextInput
import net.apptronic.test.commons_sample_compat_app.R

class EnterNameBinder : ViewBinder<EnterNameViewModel>() {

    override var layoutResId: Int? = R.layout.fragment_enter_name

    override fun onBindView(view: View, viewModel: EnterNameViewModel) {
        with(view) {
            bindTextInput(edtFirstName, viewModel.firstName)
            bindTextInput(edtLastName, viewModel.lastName)
            bindEnabledDisabled(btnContinue, viewModel.isContinueEnabled)
            bindClickListener(btnContinue, viewModel.onContinueClick)
        }
    }

}