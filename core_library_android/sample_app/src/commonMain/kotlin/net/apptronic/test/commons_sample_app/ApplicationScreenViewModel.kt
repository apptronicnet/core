package net.apptronic.test.commons_sample_app

import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.core.mvvm.viewmodel.adapter.BasicTransition
import net.apptronic.test.commons_sample_app.convert.ConvertScreenViewModel
import net.apptronic.test.commons_sample_app.debounce.createDebounceSampleViewModel
import net.apptronic.test.commons_sample_app.lazylist.createLazyListItemViewModel
import net.apptronic.test.commons_sample_app.list.ListContext
import net.apptronic.test.commons_sample_app.list.ListScreenViewModel
import net.apptronic.test.commons_sample_app.login.LoginRouter
import net.apptronic.test.commons_sample_app.login.LoginViewModel
import net.apptronic.test.commons_sample_app.login.LoginViewModelContext
import net.apptronic.test.commons_sample_app.login.RegistrationListener
import net.apptronic.test.commons_sample_app.navigation.NavigationContext
import net.apptronic.test.commons_sample_app.navigation.NavigationRouter
import net.apptronic.test.commons_sample_app.navigation.NavigationScreenViewModel
import net.apptronic.test.commons_sample_app.pager.createPagerViewModel
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
    private val parent: ApplicationScreenViewModel
) : NavigationRouter {

    override fun openLoginDemo() {
        val loginContext = LoginViewModelContext(
            parent,
            ApplicationScreenLoginRouterImpl(parent)
        )
        parent.rootPage.add(LoginViewModel(loginContext), BasicTransition.Forward)
    }

    override fun openConverterDemo() {
        parent.rootPage.add(
            ConvertScreenViewModel(ViewModelContext(parent)),
            BasicTransition.Forward
        )
    }

    override fun openListDemo() {
        parent.rootPage.add(
            ListScreenViewModel(ListContext(parent)),
            BasicTransition.Forward
        )
    }

    override fun openPagerDemo() {
        parent.rootPage.add(
            createPagerViewModel(parent),
            BasicTransition.Forward
        )
    }

    override fun openDebounceDemo() {
        parent.rootPage.add(
            createDebounceSampleViewModel(parent),
            BasicTransition.Forward
        )
    }

    override fun openLazyListDemo() {
        parent.rootPage.add(
            createLazyListItemViewModel(parent),
            BasicTransition.Forward
        )
    }

}

class ApplicationScreenLoginRouterImpl(
    private val parent: ApplicationScreenViewModel
) : LoginRouter {

    override fun openRegistrationScreen(listener: RegistrationListener) {
        val router = ApplicationScreenRegistrationRouterImpl(parent, listener)
        val registrationContext = RegistrationViewModelContext(parent, router)
        parent.rootPage.add(
            RegistrationViewModel(registrationContext),
            BasicTransition.Forward
        )
    }

}

class ApplicationScreenRegistrationRouterImpl(
    private val parent: ApplicationScreenViewModel,
    private val listener: RegistrationListener
) : RegistrationRouter {

    override fun backToLogin(preFilledLogin: String) {
        listener.onRegistrationDone(preFilledLogin)
        parent.rootPage.popBackStack(BasicTransition.Back)
    }

}