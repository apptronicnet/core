package net.apptronic.test.commons_sample_app.login

import net.apptronic.core.component.context.ComponentContext
import net.apptronic.core.component.di.Descriptor
import net.apptronic.core.component.di.declareModule
import net.apptronic.core.component.process.BackgroundAction
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.test.commons_sample_app.login.LoginViewModelContext.Companion.LoginActionDescriptor

class LoginViewModelContext(parent: ComponentContext) : ViewModelContext(parent) {

    companion object {
        val LoginActionDescriptor = Descriptor<BackgroundAction<LoginRequest, LoginResult>>()
    }

    init {
        objects().addModule(LoginModule)
    }

}

val LoginModule = declareModule {

    factory(LoginActionDescriptor) {
        LoginAction
    }

}