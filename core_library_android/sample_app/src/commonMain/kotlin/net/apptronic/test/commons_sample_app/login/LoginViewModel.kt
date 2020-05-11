package net.apptronic.test.commons_sample_app.login

import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.genericEvent
import net.apptronic.core.component.inject
import net.apptronic.core.mvvm.common.textInput
import net.apptronic.core.mvvm.viewmodel.ViewModel

class LoginViewModel(parent: Context, loginRouter: LoginRouter) :
    ViewModel(parent, loginContext(loginRouter)), RegistrationListener {

    private val loginRouter = inject(LoginRouterDescriptor)

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