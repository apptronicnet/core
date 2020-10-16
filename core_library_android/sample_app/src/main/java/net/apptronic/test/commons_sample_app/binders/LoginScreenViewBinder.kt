package net.apptronic.test.commons_sample_app.binders

import android.view.View
import kotlinx.android.synthetic.main.screen_login.view.*
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.android.viewmodel.bindings.bindClickListener
import net.apptronic.core.android.viewmodel.bindings.bindTextInput
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.login.LoginViewModel

class LoginScreenViewBinder : ViewBinder<LoginViewModel>() {

    override var layoutResId: Int? = R.layout.screen_login

    override fun onBindView(view: View, viewModel: LoginViewModel) {
        with(view) {
            bindTextInput(loginField, viewModel.login)
            bindTextInput(passwordField, viewModel.password)
            bindClickListener(loginButton, viewModel.loginClick)
            bindClickListener(registerNewAccount, viewModel.registerClick)
        }
    }

}