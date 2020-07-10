package net.apptronic.test.commons_sample_app.registration

import net.apptronic.core.component.di.dependencyDescriptor
import net.apptronic.core.mvvm.viewmodel.defineViewModelContext

val RegistrationRouterDescriptor = dependencyDescriptor<RegistrationRouter>()

fun registrationViewModelContext(router: RegistrationRouter) = defineViewModelContext {
    dependencyDispatcher.addInstance(RegistrationRouterDescriptor, router)
}