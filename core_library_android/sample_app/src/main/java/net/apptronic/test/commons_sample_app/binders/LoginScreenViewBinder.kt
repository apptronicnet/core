package net.apptronic.test.commons_sample_app.binders

import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindClickListener
import net.apptronic.core.android.viewmodel.bindings.bindTextInput
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.databinding.ScreenLoginBinding
import net.apptronic.test.commons_sample_app.login.LoginViewModel

class LoginScreenViewBinder : ViewBinder<LoginViewModel>() {

    override var layoutResId: Int? = R.layout.screen_login

    private val viewBinding by viewBinding(ScreenLoginBinding::bind)

    override fun onBindView() {
        with(viewBinding) {
            bindTextInput(loginField, viewModel.login)
            bindTextInput(passwordField, viewModel.password)
            bindClickListener(loginButton, viewModel.loginClick)
            bindClickListener(registerNewAccount, viewModel.registerClick)
        }
    }

}