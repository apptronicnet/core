package net.apptronic.test.commons_sample_app.navigation

import net.apptronic.core.component.di.dependencyDescriptor
import net.apptronic.core.mvvm.viewmodel.defineViewModelContext

val NavigationRouterDescriptor = dependencyDescriptor<NavigationRouter>()

fun navigationContext(navigationRouter: NavigationRouter) =
    defineViewModelContext("NavigationContext") {
        dependencyDispatcher.addInstance(NavigationRouterDescriptor, navigationRouter)
    }