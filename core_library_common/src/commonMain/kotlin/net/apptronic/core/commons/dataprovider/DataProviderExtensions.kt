package net.apptronic.core.commons.dataprovider

import net.apptronic.core.context.Contextual
import net.apptronic.core.context.di.*

/**
 * Inject data of type [T] using [descriptor] with [Unit] type.
 */
fun <T : Any> Contextual.injectData(descriptor: DataProviderDescriptor<Unit, T>): DataProviderProperty<T> {
    val holder =
        dependencyProvider.inject(descriptor.holderDescriptor, parameters { add(descriptor.keyDescriptor, Unit) })
    return DataProviderPropertyImpl(context, holder)
}

/**
 * Inject data of type [T] using [descriptor] with [key].
 */
fun <K : Any, T : Any> Contextual.injectData(
    descriptor: DataProviderDescriptor<K, T>,
    key: K
): DataProviderProperty<T> {
    val holder =
        dependencyProvider.inject(descriptor.holderDescriptor, parameters { add(descriptor.keyDescriptor, key) })
    return DataProviderPropertyImpl(context, holder)
}


/**
 * Add [DataProvider] dependency which managed by [SharedScope]
 *
 * To add cache use [dataProviderCache] with same [DataProviderDescriptor]
 */
fun <K : Any, T : Any> ModuleDefinition.sharedDataProvider(
    descriptor: DataProviderDescriptor<K, T>,
    fallbackCount: Int = 0,
    fallbackLifetime: Long = 0,
    builder: FactoryScope.(K) -> DataProvider<K, T>
) {
    single(descriptor.dispatcherDescriptor) {
        DataProviderDispatcher(scopedContext(), descriptor)
    }
    single(descriptor.scopeManagerDescriptor) {
        DataProviderSharedScopeManager(scopedContext(), descriptor)
    }
    shared(
        descriptor.holderDescriptor,
        fallbackCount,
        fallbackLifetime,
        managerDescriptor = descriptor.scopeManagerDescriptor
    ) {
        val key: K = provided(descriptor.keyDescriptor)
        DataProviderHolder(dataProviderContext(descriptor, key, builder), descriptor, key)
    }
}

/**
 * Add [DataProvider] dependency which managed by [SingleScope]. Can be used only with [DataProvider] with key
 * type of [Unit].
 *
 * To add cache use [dataProviderCache] with same [DataProviderDescriptor]
 */
fun <T : Any> ModuleDefinition.singleDataProvider(
    descriptor: DataProviderDescriptor<Unit, T>,
    initOnLoad: Boolean = false,
    builder: FactoryScope.() -> DataProvider<Unit, T>
) {
    val unitBuilder: FactoryScope.(Unit) -> DataProvider<Unit, T> = {
        builder()
    }
    single(descriptor.dispatcherDescriptor) {
        DataProviderDispatcher(scopedContext(), descriptor)
    }
    single(descriptor.holderDescriptor, initOnLoad = initOnLoad) {
        DataProviderHolder(dataProviderContext(descriptor, Unit, unitBuilder), descriptor, Unit)
    }
}