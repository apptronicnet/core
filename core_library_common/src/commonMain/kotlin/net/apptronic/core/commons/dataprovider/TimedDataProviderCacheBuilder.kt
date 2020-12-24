package net.apptronic.core.commons.dataprovider

import net.apptronic.core.commons.dataprovider.cache.DataProviderCache
import net.apptronic.core.commons.dataprovider.cache.TimedDataProviderCache
import net.apptronic.core.context.Context
import net.apptronic.core.context.di.Scope

fun <K, T> Scope.timedCache(
    builder: TimedDataProviderCacheBuilder<K, T>.() -> Unit = {}
): DataProviderCache<K, T> {
    return TimedDataProviderCacheBuilder<K, T>(scopedContext()).apply(builder).build()
}

class TimedDataProviderCacheBuilder<K, T> internal constructor(private val context: Context) {

    var maxSize: Int = 32
    var sizeFunction: (T) -> Int = {
        1
    }
    var expirationTime: Long = 60000L

    val Long.seconds: Long
        get() = this * 1000L

    val Long.minutes: Long
        get() = this * 60_000L

    val Long.hours: Long
        get() = this * 3600_000L

    fun sizeFunction(sizeFunction: (T) -> Int) {
        this.sizeFunction = sizeFunction
    }

    internal fun build(): DataProviderCache<K, T> {
        return TimedDataProviderCache(
            context = context,
            maxSize = maxSize,
            sizeFunction = sizeFunction,
            expirationTime = expirationTime
        )
    }

}