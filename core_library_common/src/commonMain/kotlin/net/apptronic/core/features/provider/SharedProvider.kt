package net.apptronic.core.features.provider

import net.apptronic.core.component.Component
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.ContextDefinition
import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.context.EmptyContext
import net.apptronic.core.component.di.*
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.inject

inline fun <reified T> providerDependencyDescriptor(): DependencyDescriptor<Provider<T>> {
    return dependencyDescriptor<Provider<T>>()
}

inline fun <reified T, reified K : Any> Contextual.injectFromProvider(descriptor: DependencyDescriptor<Provider<T>>, key: K): Entity<T> {
    return inject(descriptor, parameters { add(key) }).provide(context)
}

inline fun <T, reified K : Any> ModuleDefinition.sharedProvider(
        descriptor: DependencyDescriptor<Provider<T>>,
        noinline builder: SharedScope.(K) -> Provider<T>
) {
    shared(descriptor) {
        val key: K = inject(K::class)
        builder(key)
    }
}

inline fun <T, reified K : Any> ModuleDefinition.sharedProviderBuilder(
        descriptor: DependencyDescriptor<Provider<T>>,
        contextDefinition: ContextDefinition<Context> = EmptyContext,
        noinline builder: Component.(K) -> Entity<T>
) {
    shared(descriptor) {
        val context = scopedContext(contextDefinition)
        val key: K = inject(K::class)
        SimpleProvider(context, key, builder)
    }
}