package net.apptronic.core.commons.navigation

import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.context.childContext
import net.apptronic.core.component.di.ModuleDefinition
import net.apptronic.core.component.di.SingleScope
import net.apptronic.core.component.inject

typealias DefaultNavigationHandler = NavigationHandler<Any>

val DefaultNavigationRouterDescriptor = navigationRouterDescriptor<Any>()

fun Contextual.hostNavigationRouter(): NavigationRouter<Any> {
    return hostNavigationRouter(DefaultNavigationRouterDescriptor, BasicNavigationRouter(childContext()))
}

fun <T> Contextual.hostNavigationRouter(
        descriptor: NavigationRouterDescriptor<T>, builder: Contextual.() -> NavigationRouter<T>
): NavigationRouter<T> {
    val router = builder()
    return hostNavigationRouter(descriptor, router)
}

fun <T> Contextual.hostNavigationRouter(
        descriptor: NavigationRouterDescriptor<T>, router: NavigationRouter<T> = BasicNavigationRouter(childContext())
): NavigationRouter<T> {
    context.dependencyDispatcher.addInstance(descriptor.dependencyDescriptor, router)
    return router
}

fun ModuleDefinition.navigationRouter() {
    navigationRouter(DefaultNavigationRouterDescriptor) {
        BasicNavigationRouter(scopedContext())
    }
}

fun <T> ModuleDefinition.navigationRouter(descriptor: NavigationRouterDescriptor<T>) {
    navigationRouter(descriptor) {
        BasicNavigationRouter(scopedContext())
    }
}

fun <T> ModuleDefinition.navigationRouter(
        descriptor: NavigationRouterDescriptor<T>, builder: SingleScope.() -> NavigationRouter<T>
) {
    single(descriptor.dependencyDescriptor, builder)
}

fun Contextual.injectNavigationRouter(): NavigationRouter<Any> {
    return injectNavigationRouter(DefaultNavigationRouterDescriptor)
}

fun <T> Contextual.injectNavigationRouter(descriptor: NavigationRouterDescriptor<T>): NavigationRouter<T> {
    return inject(descriptor.dependencyDescriptor)
}

fun Contextual.registerNavigationHandler(handler: DefaultNavigationHandler) {
    registerNavigationHandler(DefaultNavigationRouterDescriptor, handler)
}

fun <T> Contextual.registerNavigationHandler(descriptor: NavigationRouterDescriptor<T>, handler: NavigationHandler<T>) {
    val router = inject(descriptor.dependencyDescriptor)
    router.registerHandler(context, handler)
}