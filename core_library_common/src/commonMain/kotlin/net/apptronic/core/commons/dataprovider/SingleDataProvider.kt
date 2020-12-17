package net.apptronic.core.commons.dataprovider

import net.apptronic.core.context.di.ModuleDefinition
import net.apptronic.core.context.di.SingleScope

/**
 * Add [DataProvider] dependency which managed by [SingleScope]. Can be used only with [DataProvider] with key
 * type of [Unit].
 *
 * To add cache use [dataProviderCache] with same [DataProviderDescriptor]
 */
fun <T : Any> ModuleDefinition.singleDataProvider(
        descriptor: DataProviderDescriptor<Unit, T>,
        initOnLoad: Boolean = false,
        builder: SingleScope.() -> DataProvider<Unit, T>
) {
    single(descriptor.holderDescriptor) {
        val provider = builder()
        val cache = optional(descriptor.cacheDescriptor)
        DataProviderHolder<Unit, T>(scopedContext(), Unit, provider, cache)
    }.also {
        if (initOnLoad) {
            it.initOnLoad()
        }
    }
}