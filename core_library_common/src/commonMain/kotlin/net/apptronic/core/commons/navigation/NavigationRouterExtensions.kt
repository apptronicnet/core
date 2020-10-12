package net.apptronic.core.commons.navigation

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.context.childContext
import net.apptronic.core.component.di.ModuleDefinition
import net.apptronic.core.component.di.SingleScope
import net.apptronic.core.component.inject

typealias DefaultNavigationHandler = NavigationHandler<Any>

@UnderDevelopment
val DefaultNavigationRouterDescriptor = navigationRouterDescriptor<Any>()

@UnderDevelopment
fun Contextual.hostNavigationRouter(): NavigationRouter<Any> {
    return hostNavigationRouter(DefaultNavigationRouterDescriptor, BasicNavigationRouter(childContext()))
}

@UnderDevelopment
fun <T> Contextual.hostNavigationRouter(
        descriptor: NavigationRouterDescriptor<T>, builder: Contextual.() -> NavigationRouter<T>
): NavigationRouter<T> {
    val router = builder()
    return hostNavigationRouter(descriptor, router)
}

@UnderDevelopment
fun <T> Contextual.hostNavigationRouter(
        descriptor: NavigationRouterDescriptor<T>, router: NavigationRouter<T> = BasicNavigationRouter(childContext())
): NavigationRouter<T> {
    context.dependencyDispatcher.addInstance(descriptor.dependencyDescriptor, router)
    return router
}

@UnderDevelopment
fun ModuleDefinition.navigationRouter() {
    navigationRouter(DefaultNavigationRouterDescriptor) {
        BasicNavigationRouter(scopedContext())
    }
}

@UnderDevelopment
fun <T> ModuleDefinition.navigationRouter(descriptor: NavigationRouterDescriptor<T>) {
    navigationRouter(descriptor) {
        BasicNavigationRouter(scopedContext())
    }
}

@UnderDevelopment
fun <T> ModuleDefinition.navigationRouter(
        descriptor: NavigationRouterDescriptor<T>, builder: SingleScope.() -> NavigationRouter<T>
) {
    single(descriptor.dependencyDescriptor, builder)
}

@UnderDevelopment
fun Contextual.injectNavigationRouter(): NavigationRouter<Any> {
    return injectNavigationRouter(DefaultNavigationRouterDescriptor)
}

@UnderDevelopment
fun <T> Contextual.injectNavigationRouter(descriptor: NavigationRouterDescriptor<T>): NavigationRouter<T> {
    return inject(descriptor.dependencyDescriptor)
}

@UnderDevelopment
fun Contextual.registerNavigationHandler(handler: DefaultNavigationHandler) {
    registerNavigationHandler(DefaultNavigationRouterDescriptor, handler)
}

@UnderDevelopment
fun <T> Contextual.registerNavigationHandler(descriptor: NavigationRouterDescriptor<T>, handler: NavigationHandler<T>) {
    val router = inject(descriptor.dependencyDescriptor)
    router.registerHandler(context, handler)
}