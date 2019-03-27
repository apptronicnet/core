package net.apptronic.test.commons_sample_app

import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.mvvm.viewmodel.adapter.BasicTransition
import net.apptronic.test.commons_sample_app.login.LoginRouter
import net.apptronic.test.commons_sample_app.login.LoginViewModel
import net.apptronic.test.commons_sample_app.login.LoginViewModelContext
import net.apptronic.test.commons_sample_app.registration.RegistrationViewModel
import net.apptronic.test.commons_sample_app.registration.RegistrationViewModelContext

class ApplicationScreenViewModel(context: ViewModelContext) : ViewModel(context) {

    val mainScreen = subModelContainer()

    init {
        val loginContext = LoginViewModelContext(
            this,
            ApplicationScreenLoginRouter(this)
        )
        mainScreen.set(LoginViewModel(loginContext))
    }

    fun onBackPressed(actionIfEmpty: () -> Unit) {
        mainScreen.navigateBack(BasicTransition.Back, actionIfEmpty)
    }

}

class ApplicationScreenLoginRouter(
    private val viewModel: ApplicationScreenViewModel
) : LoginRouter {

    override fun openRegistrationScreen() {
        val registrationContext = RegistrationViewModelContext(viewModel)
        viewModel.mainScreen.add(
            RegistrationViewModel(registrationContext),
            BasicTransition.Forward
        )
    }

}