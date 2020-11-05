package net.apptronic.core.commons.routing

import net.apptronic.core.context.di.DependencyDescriptor
import net.apptronic.core.context.di.dependencyDescriptor

fun <T> navigationRouterDescriptor(): NavigationRouterDescriptor<T> {
    return NavigationRouterDescriptor(dependencyDescriptor())
}

class NavigationRouterDescriptor<T> internal constructor(
        val dependencyDescriptor: DependencyDescriptor<NavigationRouter<T>>
)