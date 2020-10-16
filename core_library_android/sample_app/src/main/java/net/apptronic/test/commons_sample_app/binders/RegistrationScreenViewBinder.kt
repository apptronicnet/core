package net.apptronic.test.commons_sample_app.binders

import android.view.View
import kotlinx.android.synthetic.main.screen_registration.view.*
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindClickListener
import net.apptronic.core.android.viewmodel.bindings.bindEnabledDisabled
import net.apptronic.core.android.viewmodel.bindings.bindTextInput
import net.apptronic.core.android.viewmodel.bindings.bindVisibleGone
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.registration.RegistrationViewModel

class RegistrationScreenViewBinder : ViewBinder<RegistrationViewModel>() {

    override var layoutResId: Int? = R.layout.screen_registration

    override fun onBindView(view: View, viewModel: RegistrationViewModel) {
        with(view) {
            bindTextInput(email, viewModel.email)
            bindTextInput(password, viewModel.password)
            bindTextInput(passwordConfirmation, viewModel.passwordConfirmation)
            bindClickListener(registerButton, viewModel.registerButtonClickEvent)
            bindEnabledDisabled(registerButton, viewModel.registerButtonEnabled)
            bindVisibleGone(passwordConfirmationError, viewModel.showPasswordsDoesNotMatchError)
        }
    }
}