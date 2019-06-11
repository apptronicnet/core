package net.apptronic.test.commons_sample_app.login

import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.mvvm.common.textInput
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.test.commons_sample_app.login.LoginViewModelContext.Companion.LoginRouterDescriptor

class LoginViewModel(context: ViewModelContext) : ViewModel(context), RegistrationListener {

    private val loginRouter = getProvider().inject(LoginRouterDescriptor)

    val login = textInput("")

    val password = textInput("")

    val loginClick = genericEvent()

    val registerClick = genericEvent()

    init {
        registerClick.subscribe {
            loginRouter.openRegistrationScreen(this)
        }
    }

    override fun onRegistrationDone(preFilledLogin: String) {
        login.setText(preFilledLogin)
        password.setText("")
    }

}