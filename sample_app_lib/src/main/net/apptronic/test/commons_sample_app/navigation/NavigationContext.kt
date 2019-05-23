package net.apptronic.test.commons_sample_app.navigation

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.di.createDescriptor
import net.apptronic.core.mvvm.viewmodel.ViewModelContext

class NavigationContext(
    parent: Context,
    navigationRouter: NavigationRouter
) : ViewModelContext(parent) {

    companion object {
        val NavigationRouterDescriptor = createDescriptor<NavigationRouter>()
    }

    init {
        getProvider().addInstance(NavigationRouterDescriptor, navigationRouter)
    }

}