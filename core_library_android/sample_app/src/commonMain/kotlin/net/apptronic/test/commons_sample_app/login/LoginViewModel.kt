package net.apptronic.test.commons_sample_app.login

import net.apptronic.core.commons.routing.injectNavigationRouter
import net.apptronic.core.context.Contextual
import net.apptronic.core.entity.genericEvent
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.ViewModelContext
import net.apptronic.core.viewmodel.commons.textInput
import net.apptronic.core.viewmodel.viewModelContext
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