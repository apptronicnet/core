package net.apptronic.test.commons_sample_app.registration

import android.view.View
import kotlinx.android.synthetic.main.screen_registration.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.ViewModelBinding
import net.apptronic.core.android.viewmodel.bindings.ClickEventBinding
import net.apptronic.core.android.viewmodel.bindings.EnableDisableBinding
import net.apptronic.core.android.viewmodel.bindings.InputFieldBinding
import net.apptronic.core.android.viewmodel.bindings.VisibleGoneBinding
import net.apptronic.test.commons_sample_app.R

class RegistrationScreenView : AndroidView<RegistrationViewModel>() {

    init {
        layoutResId = R.layout.screen_registration
    }

    override fun onCreateBinding(
        view: View,
        viewModel: RegistrationViewModel
    ): ViewModelBinding<RegistrationViewModel> {
        return createBinding(view, viewModel) {
            with(view) {
                email.bindTo(InputFieldBinding(), viewModel.email)
                password.bindTo(InputFieldBinding(), viewModel.password)
                passwordConfirmation.bindTo(InputFieldBinding(), viewModel.passwordConfirmation)
                registerButton.bindTo(ClickEventBinding(), viewModel.registerButtonClickEvent)
                registerButton.bindTo(EnableDisableBinding(), viewModel.registerButtonEnabled)
                passwordConfirmationError.bindTo(
                    VisibleGoneBinding(),
                    viewModel.showPasswordsDoesNotMatchError
                )
            }
        }
    }

}