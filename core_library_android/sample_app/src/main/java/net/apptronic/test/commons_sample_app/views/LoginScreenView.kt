package net.apptronic.test.commons_sample_app.views

import android.view.View
import kotlinx.android.synthetic.main.screen_login.view.*
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.bindings.sendClicksTo
import net.apptronic.core.android.viewmodel.bindings.textInputBinding
import net.apptronic.test.commons_sample_app.R
import net.apptronic.test.commons_sample_app.login.LoginViewModel

class LoginScreenView : AndroidView<LoginViewModel>() {

    override var layoutResId: Int? = R.layout.screen_login

    override fun onBindView(view: View, viewModel: LoginViewModel) {
        with(view) {
            +textInputBinding(loginField, viewModel.login)
            +textInputBinding(passwordField, viewModel.password)
            +(loginButton sendClicksTo viewModel.loginClick)
            +(registerNewAccount sendClicksTo viewModel.registerClick)
        }
    }

}