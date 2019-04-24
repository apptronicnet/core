package net.apptronic.test.commons_sample_app.registration

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.di.createDescriptor
import net.apptronic.core.mvvm.viewmodel.ViewModelContext

class RegistrationViewModelContext(parent: Context, router: RegistrationRouter) :
    ViewModelContext(parent) {

    companion object {
        val RegistrationRouterDescriptor = createDescriptor<RegistrationRouter>()
    }

    init {
        getProvider().addInstance(RegistrationRouterDescriptor, router)
    }

}