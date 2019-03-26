package net.apptronic.test.commons_sample_app.login

import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.test.commons_sample_app.login.LoginViewModelContext.Companion.LoginActionDescriptor

class LoginViewModel(context: ViewModelContext) : ViewModel(context) {

    private val loginAction = objects().get(LoginActionDescriptor)

    val login = value("")

    val password = value("")

    val loginClick = genericEvent()

    private val loginProcess = backgroundProcess(LoginAction)

}