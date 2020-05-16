package net.apptronic.test.commons_sample_app.navigation

import net.apptronic.core.component.di.createDescriptor
import net.apptronic.core.mvvm.viewmodel.defineViewModelContext

val NavigationRouterDescriptor = createDescriptor<NavigationRouter>()

fun navigationContext(navigationRouter: NavigationRouter) =
    defineViewModelContext("NavigationContext") {
        dependencyDispatcher.addInstance(NavigationRouterDescriptor, navigationRouter)
    }