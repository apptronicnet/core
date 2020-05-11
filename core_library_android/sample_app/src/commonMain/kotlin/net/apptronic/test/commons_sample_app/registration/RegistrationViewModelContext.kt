package net.apptronic.test.commons_sample_app.registration

import net.apptronic.core.component.di.createDescriptor
import net.apptronic.core.mvvm.viewmodel.defineViewModelContext

val RegistrationRouterDescriptor = createDescriptor<RegistrationRouter>()

fun registrationViewModelContext(router: RegistrationRouter) = defineViewModelContext {
    dependencyDispatcher().addInstance(RegistrationRouterDescriptor, router)
}