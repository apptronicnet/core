package net.apptronic.test.commons_sample_app.views

import android.view.View
import kotlinx.android.synthetic.main.screen_registration.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.bindings.asInputFor
import net.apptronic.core.android.viewmodel.bindings.sendClicksTo
import net.apptronic.core.android.viewmodel.bindings.setEnabledDisabledFrom
import net.apptronic.core.android.viewmodel.bindings.setVisibleGoneFrom
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.registration.RegistrationViewModel

class RegistrationScreenView : AndroidView<RegistrationViewModel>() {

    override var layoutResId: Int? = R.layout.screen_registration

    override fun onBindView(view: View, viewModel: RegistrationViewModel) {
        with(view) {
            +(email asInputFor viewModel.email)
            +(password asInputFor viewModel.password)
            +(passwordConfirmation asInputFor viewModel.passwordConfirmation)
            +(registerButton sendClicksTo viewModel.registerButtonClickEvent)
            +(registerButton setEnabledDisabledFrom viewModel.registerButtonEnabled)
            +(passwordConfirmationError setVisibleGoneFrom viewModel.showPasswordsDoesNotMatchError)
        }
    }
}