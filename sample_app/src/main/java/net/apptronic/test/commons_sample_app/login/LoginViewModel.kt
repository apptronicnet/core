package net.apptronic.test.commons_sample_app.login

import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.test.commons_sample_app.login.LoginViewModelContext.Companion.LoginActionDescriptor
import net.apptronic.test.commons_sample_app.login.LoginViewModelContext.Companion.LoginRouterDescriptor

class LoginViewModel(context: ViewModelContext) : ViewModel(context), RegistrationListener {

    private val loginRouter = getProvider().inject(LoginRouterDescriptor)
    private val loginAction = getProvider().inject(LoginActionDescriptor)

    val login = value("")

    val password = value("")

    val loginClick = genericEvent()

    val registerClick = genericEvent()

    private val loginProcess = backgroundProcess(LoginAction)

    init {
        registerClick.subscribe {
            loginRouter.openRegistrationScreen(this)
        }
    }

    override fun onRegistrationDone(preFilledLogin: String) {
        login.set(preFilledLogin)
        password.set("")
    }

}