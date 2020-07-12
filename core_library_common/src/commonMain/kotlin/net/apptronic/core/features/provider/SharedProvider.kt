package net.apptronic.core.features.provider

import net.apptronic.core.component.Component
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.ContextDefinition
import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.context.EmptyContext
import net.apptronic.core.component.di.ModuleDefinition
import net.apptronic.core.component.di.SharedScope
import net.apptronic.core.component.di.parameters
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.inject
import kotlin.reflect.KClass

inline fun <reified T : Any, reified K : Any> Contextual.injectData(descriptor: DataProviderDescriptor<T, K>, key: K): Entity<T> {
    return injectData(T::class, K::class, descriptor, key)
}

fun <T : Any, K : Any> Contextual.injectData(type: KClass<T>, keyType: KClass<K>, descriptor: DataProviderDescriptor<T, K>, key: K): Entity<T> {
    return inject(descriptor.providerDescriptor, parameters { add(descriptor.keyDescriptor, key) }).provide(context)
}

inline fun <T : Any, reified K : Any> ModuleDefinition.sharedDataProvider(
        descriptor: DataProviderDescriptor<T, K>,
        noinline builder: SharedScope.(K) -> DataProvider<T>
) {
    shared(descriptor.providerDescriptor) {
        val key: K = provided(descriptor.keyDescriptor)
        builder(key)
    }
}

inline fun <reified T : Any, reified K : Any> ModuleDefinition.sharedProviderBuilder(
        descriptor: DataProviderDescriptor<T, K>,
        contextDefinition: ContextDefinition<Context> = EmptyContext,
        noinline builder: Component.(K) -> Entity<T>
) {
    shared(descriptor.providerDescriptor) {
        val context = scopedContext(contextDefinition)
        val key: K = provided(descriptor.keyDescriptor)
        SimpleDataProvider(context, key, builder)
    }
}