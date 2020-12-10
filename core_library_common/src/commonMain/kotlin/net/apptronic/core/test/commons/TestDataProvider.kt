package net.apptronic.core.test.commons

import net.apptronic.core.commons.dataprovider.DataProvider
import net.apptronic.core.commons.dataprovider.DataProviderDescriptor
import net.apptronic.core.commons.dataprovider.sharedDataProvider
import net.apptronic.core.context.Context
import net.apptronic.core.context.di.ModuleDefinition
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.commons.value

/**
 * Add data provider which returns single [value] when injected using [requestKey]
 */
fun <K : Any, T : Any> ModuleDefinition.testDataProvider(
        descriptor: DataProviderDescriptor<K, T>,
        requestKey: K, value: T
) {
    sharedDataProvider(descriptor) { key ->
        TestDataProvider<K, T>(scopedContext(), key).apply {
            if (key == requestKey) {
                dataProviderEntity.set(value)
            }
        }
    }
}

/**
 * Add data provider which returns single values according to returned by [source]
 */
fun <K : Any, T : Any> ModuleDefinition.testDataProvider(
        descriptor: DataProviderDescriptor<K, T>,
        source: (K) -> T
) {
    sharedDataProvider(descriptor) { key ->
        TestDataProvider<K, T>(scopedContext(), key).apply {
            dataProviderEntity.set(source(key))
        }
    }
}

/**
 * Add data provider which returns data from each [Entity] from [pairs] according to keys
 */
fun <K : Any, T : Any> ModuleDefinition.testDataProvider(
        descriptor: DataProviderDescriptor<K, T>,
        vararg pairs: Pair<K, Entity<T>>
) {
    sharedDataProvider(descriptor) { key ->
        TestDataProvider<K, T>(scopedContext(), key).apply {
            pairs.firstOrNull { it.first == key }?.let {
                it.second.subscribe(dataProviderEntity)
            }
        }
    }
}

open class TestDataProvider<K, T>(context: Context, key: K) : DataProvider<K, T>(context, key) {

    override val dataProviderEntity = value<T>()

}