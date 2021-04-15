package net.apptronic.test.commons_sample_app.login

import net.apptronic.core.commons.routing.injectNavigationRouter
import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.childContext
import net.apptronic.core.entity.commons.genericEvent
import net.apptronic.core.viewmodel.ViewModel
import net.apptronic.core.viewmodel.commons.textInput
import net.apptronic.test.commons_sample_app.OpenRegistrationScreen

fun Contextual.loginViewModel() = LoginViewModel(childContext())

class LoginViewModel internal constructor(context: Context) : ViewModel(context),
    RegistrationListener {

    private val router = injectNavigationRouter()

    val login = textInput("")

    val password = textInput("")

    val loginClick = genericEvent()

    val registerClick = genericEvent()

    init {
        registerClick.subscribe {
            router.sendCommandsSync(OpenRegistrationScreen(this))
        }
    }

    override fun onRegistrationDone(preFilledLogin: String) {
        login.setText(preFilledLogin)
        password.setText("")
    }

}