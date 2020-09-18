package net.apptronic.test.commons_sample_app.login

import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.commons.navigation.injectNavigationRouter
import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.context.viewModelContext
import net.apptronic.core.component.genericEvent
import net.apptronic.core.mvvm.common.textInput
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.test.commons_sample_app.OpenRegistrationScreen

fun Contextual.loginViewModel() = LoginViewModel(viewModelContext())

class LoginViewModel internal constructor(context: ViewModelContext) : ViewModel(context),
    RegistrationListener {

    private val router = injectNavigationRouter()

    val login = textInput("")

    val password = textInput("")

    val loginClick = genericEvent()

    val registerClick = genericEvent()

    init {
        registerClick.subscribe {
            router.sendCommands(OpenRegistrationScreen(this))
        }
    }

    override fun onRegistrationDone(preFilledLogin: String) {
        login.setText(preFilledLogin)
        password.setText("")
    }

}