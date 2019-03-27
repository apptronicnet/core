package net.apptronic.test.commons_sample_app.login

import net.apptronic.core.component.context.ComponentContext
import net.apptronic.core.component.di.Descriptor
import net.apptronic.core.component.di.declareModule
import net.apptronic.core.component.process.BackgroundAction
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.test.commons_sample_app.login.LoginViewModelContext.Companion.LoginActionDescriptor

class LoginViewModelContext(
    parent: ComponentContext,
    val loginRouter: LoginRouter
) : ViewModelContext(parent) {

    companion object {
        val LoginActionDescriptor = Descriptor<BackgroundAction<LoginRequest, LoginResult>>()
        val LoginRouterDescriptor = Descriptor<LoginRouter>()
    }

    init {
        objects().addModule(LoginModule)
        objects().addInstance(loginRouter, LoginRouterDescriptor)
    }

}

val LoginModule = declareModule {

    factory(LoginActionDescriptor) {
        LoginAction
    }

}