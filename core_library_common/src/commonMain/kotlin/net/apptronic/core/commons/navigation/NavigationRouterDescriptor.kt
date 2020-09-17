package net.apptronic.core.commons.navigation

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.component.di.DependencyDescriptor
import net.apptronic.core.component.di.dependencyDescriptor

@UnderDevelopment
fun <T> navigationRouterDescriptor(): NavigationRouterDescriptor<T> {
    return NavigationRouterDescriptor(dependencyDescriptor())
}

@UnderDevelopment
class NavigationRouterDescriptor<T> internal constructor(
        val dependencyDescriptor: DependencyDescriptor<NavigationRouter<T>>
)