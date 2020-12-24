package net.apptronic.core.commons.dataprovider

import net.apptronic.core.commons.dataprovider.cache.DataProviderCache
import net.apptronic.core.commons.dataprovider.cache.SimpleDataProviderCache
import net.apptronic.core.context.di.Scope

fun <K, T> Scope.simpleCache(
    builder: SimpleDataProviderCacheBuilder<K, T>.() -> Unit = {}
): DataProviderCache<K, T> {
    return SimpleDataProviderCacheBuilder<K, T>().apply(builder).build()
}

class SimpleDataProviderCacheBuilder<K, T> internal constructor() {

    var maxSize: Int = 32
    var sizeFunction: (T) -> Int = {
        1
    }

    fun sizeFunction(sizeFunction: (T) -> Int) {
        this.sizeFunction = sizeFunction
    }

    internal fun build(): DataProviderCache<K, T> {
        return SimpleDataProviderCache(
            maxSize = maxSize,
            sizeFunction = sizeFunction
        )
    }

}