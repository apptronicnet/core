package net.apptronic.test.commons_sample_app.login

import net.apptronic.core.component.context.ComponentContext
import net.apptronic.core.component.di.createDescriptor
import net.apptronic.core.component.di.declareModule
import net.apptronic.core.component.process.BackgroundAction
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.test.commons_sample_app.login.LoginViewModelContext.Companion.LoginActionDescriptor

class LoginViewModelContext(
    parent: ComponentContext,
    loginRouter: LoginRouter
) : ViewModelContext(parent) {

    companion object {
        val LoginActionDescriptor = createDescriptor<BackgroundAction<LoginRequest, LoginResult>>()
        val LoginRouterDescriptor = createDescriptor<LoginRouter>()
    }

    init {
        objects().addModule(LoginModule)
        objects().addInstance(LoginRouterDescriptor, loginRouter)
    }

}

val LoginModule = declareModule {

    factory(LoginActionDescriptor) {
        LoginAction
    }

}