package net.apptronic.test.commons_sample_app.login

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.apptronic.core.android.viewmodel.*
import net.apptronic.test.commons_sample_app.R

class LoginScreenView(viewModel: LoginViewModel) : AndroidView<LoginViewModel>(viewModel) {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.screen_login, container, false)
    }

    override fun onBindView() {
        editText(R.id.loginField).bindTo(viewModel.login)
        editText(R.id.passwordField).bindTo(viewModel.password)
        view(R.id.loginButton).bindOnClickListener(viewModel.loginClick)
        view(R.id.registerNewAccount).bindOnClickListener(viewModel.registerClick)
    }

}