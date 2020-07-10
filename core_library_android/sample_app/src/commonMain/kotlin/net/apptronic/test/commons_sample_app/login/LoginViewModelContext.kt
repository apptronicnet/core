package net.apptronic.test.commons_sample_app.login

import net.apptronic.core.component.di.declareModule
import net.apptronic.core.component.di.dependencyDescriptor
import net.apptronic.core.mvvm.viewmodel.defineViewModelContext

val LoginRouterDescriptor = dependencyDescriptor<LoginRouter>()

fun loginContext(
    loginRouter: LoginRouter
) = defineViewModelContext("Login") {
    dependencyDispatcher.addModule(LoginModule)
    dependencyDispatcher.addInstance(LoginRouterDescriptor, loginRouter)

}

private val LoginModule = declareModule {

}