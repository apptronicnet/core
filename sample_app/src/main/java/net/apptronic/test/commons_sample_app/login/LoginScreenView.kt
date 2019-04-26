package net.apptronic.test.commons_sample_app.login

import android.view.View
import kotlinx.android.synthetic.main.screen_login.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.ViewModelBinding
import net.apptronic.core.android.viewmodel.bindings.ClickEventBinding
import net.apptronic.core.android.viewmodel.bindings.InputFieldBinding
import net.apptronic.test.commons_sample_app.R

class LoginScreenView : AndroidView<LoginViewModel>() {

    init {
        layoutResId = R.layout.screen_login
    }

    override fun onCreateBinding(
        view: View,
        viewModel: LoginViewModel
    ): ViewModelBinding<LoginViewModel> {
        return createBinding(view, viewModel) {
            with(view) {
                loginField.bindTo(InputFieldBinding(), viewModel.login)
                passwordField.bindTo(InputFieldBinding(), viewModel.password)
                loginButton.bindTo(ClickEventBinding(), viewModel.loginClick)
                registerNewAccount.bindTo(ClickEventBinding(), viewModel.registerClick)
            }
        }
    }

}