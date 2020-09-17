package net.apptronic.core.commons.navigation

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.context.childContext
import net.apptronic.core.component.di.ModuleDefinition
import net.apptronic.core.component.di.SingleScope
import net.apptronic.core.component.inject

@UnderDevelopment
val DefaultNavigationRouterDescriptor = navigationRouterDescriptor<Any>()

@UnderDevelopment
fun Contextual.hostNavigationRouter(): NavigationRouter<Any> {
    return hostNavigationRouter(DefaultNavigationRouterDescriptor, BaseNavigationRouter(childContext()))
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
        descriptor: NavigationRouterDescriptor<T>, router: NavigationRouter<T>
): NavigationRouter<T> {
    context.dependencyDispatcher.addInstance(descriptor.dependencyDescriptor, router)
    return router
}

@UnderDevelopment
fun ModuleDefinition.hostNavigationRouter() {
    hostNavigationRouter(DefaultNavigationRouterDescriptor) {
        BaseNavigationRouter(scopedContext())
    }
}

@UnderDevelopment
fun <T> ModuleDefinition.hostNavigationRouter(
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
fun Contextual.registerNavigationCallback(callback: NavigationCallback<Any>) {
    registerNavigationCallback(DefaultNavigationRouterDescriptor, callback)
}

@UnderDevelopment
fun <T> Contextual.registerNavigationCallback(descriptor: NavigationRouterDescriptor<T>, callback: NavigationCallback<T>) {
    val router = inject(descriptor.dependencyDescriptor)
    router.registerCallback(context, callback)
}