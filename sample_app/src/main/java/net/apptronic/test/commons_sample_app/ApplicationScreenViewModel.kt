package net.apptronic.test.commons_sample_app

import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.mvvm.viewmodel.adapter.BasicTransition
import net.apptronic.test.commons_sample_app.login.LoginRouter
import net.apptronic.test.commons_sample_app.login.LoginViewModel
import net.apptronic.test.commons_sample_app.login.LoginViewModelContext
import net.apptronic.test.commons_sample_app.login.RegistrationListener
import net.apptronic.test.commons_sample_app.navigation.NavigationContext
import net.apptronic.test.commons_sample_app.navigation.NavigationRouter
import net.apptronic.test.commons_sample_app.navigation.NavigationScreenViewModel
import net.apptronic.test.commons_sample_app.registration.RegistrationRouter
import net.apptronic.test.commons_sample_app.registration.RegistrationViewModel
import net.apptronic.test.commons_sample_app.registration.RegistrationViewModelContext

class ApplicationScreenViewModel(context: ViewModelContext) : ViewModel(context) {

    val rootPage = stackNavigator()

    init {
        val router = ApplicationScreenNavigationRouterImpl(this)
        val navigationContext = NavigationContext(this, router)
        rootPage.set(NavigationScreenViewModel(navigationContext))
    }

    fun onBackPressed(actionIfEmpty: () -> Unit) {
        rootPage.navigateBack(BasicTransition.Back, actionIfEmpty)
    }

}

class ApplicationScreenNavigationRouterImpl(
    private val viewModel: ApplicationScreenViewModel
) : NavigationRouter {

    override fun openLoginDemo() {
        val loginContext = LoginViewModelContext(
            viewModel,
            ApplicationScreenLoginRouterImpl(viewModel)
        )
        viewModel.rootPage.add(LoginViewModel(loginContext), BasicTransition.Forward)
    }

}

class ApplicationScreenLoginRouterImpl(
    private val viewModel: ApplicationScreenViewModel
) : LoginRouter {

    override fun openRegistrationScreen(listener: RegistrationListener) {
        val router = ApplicationScreenRegistrationRouterImpl(viewModel, listener)
        val registrationContext = RegistrationViewModelContext(viewModel, router)
        viewModel.rootPage.add(
            RegistrationViewModel(registrationContext),
            BasicTransition.Forward
        )
    }

}

class ApplicationScreenRegistrationRouterImpl(
    private val viewModel: ApplicationScreenViewModel,
    private val listener: RegistrationListener
) : RegistrationRouter {

    override fun backToLogin(preFilledLogin: String) {
        listener.onRegistrationDone(preFilledLogin)
        viewModel.rootPage.popBackStack(BasicTransition.Back)
    }

}