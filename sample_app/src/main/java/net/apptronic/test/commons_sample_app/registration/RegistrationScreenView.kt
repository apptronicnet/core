package net.apptronic.test.commons_sample_app.registration

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.apptronic.core.android.viewmodel.*
import net.apptronic.test.commons_sample_app.R

class RegistrationScreenView(viewModel: RegistrationViewModel) :
    AndroidView<RegistrationViewModel>(viewModel) {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.screen_registration, container, false)
    }

    override fun onBindView() {
        editText(R.id.email).bindTo(viewModel.email)
        editText(R.id.password).bindTo(viewModel.password)
        editText(R.id.passwordConfirmation).bindTo(viewModel.passwordConfirmation)
        view(R.id.registerButton).bindOnClickListener(viewModel.registerButtonClickEvent)
        view(R.id.registerButton).bindAsEnabledDisabled(viewModel.registerButtonEnabled)
        view(R.id.passwordConfirmationError).bindAsVisibleInvisible(viewModel.showPasswordsDoesNotMatchError)
    }

}