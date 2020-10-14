package net.apptronic.core.commons.navigation

import net.apptronic.core.component.di.DependencyDescriptor
import net.apptronic.core.component.di.dependencyDescriptor

fun <T> navigationRouterDescriptor(): NavigationRouterDescriptor<T> {
    return NavigationRouterDescriptor(dependencyDescriptor())
}

class NavigationRouterDescriptor<T> internal constructor(
        val dependencyDescriptor: DependencyDescriptor<NavigationRouter<T>>
)