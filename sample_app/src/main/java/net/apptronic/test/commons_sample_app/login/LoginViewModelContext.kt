package net.apptronic.test.commons_sample_app.login

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.di.createDescriptor
import net.apptronic.core.component.di.declareModule
import net.apptronic.core.mvvm.viewmodel.ViewModelContext

class LoginViewModelContext(
    parent: Context,
    loginRouter: LoginRouter
) : ViewModelContext(parent) {

    companion object {
        val LoginRouterDescriptor = createDescriptor<LoginRouter>()
    }

    init {
        getProvider().addModule(LoginModule)
        getProvider().addInstance(LoginRouterDescriptor, loginRouter)
    }

}

val LoginModule = declareModule {

}