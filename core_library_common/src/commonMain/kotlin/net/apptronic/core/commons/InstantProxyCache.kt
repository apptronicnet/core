package net.apptronic.core.commons

import kotlinx.coroutines.Job
import net.apptronic.core.UnderDevelopment
import net.apptronic.core.base.subject.ValueHolder
import net.apptronic.core.commons.cache.CacheComponent
import net.apptronic.core.context.Context

/**
 * Base cache class which stays on front of some another cache assuming that cache is "instant" meaning
 * caches values in-memory without background operations. In case when [instantCache] cannot return value then
 * value is get from [targetCache] and all values set to both [instantCache] and [targetCache]. Target cache can be
 * storage cache which takes longer to get/set but keeps it's data after process restart.
 */
@UnderDevelopment
class InstantProxyCache<K, T>(
        context: Context,
        private val instantCache: CacheComponent<K, T>,
        private val targetCache: CacheComponent<K, T>,
        private val releaseTargetCache: Boolean = false
) : CacheComponent<K, T>(context) {

    override operator fun get(key: K): ValueHolder<T>? {
        return instantCache[key] ?: targetCache[key]
    }

    override fun getAsync(key: K, target: (T) -> Unit): Job? {
        val local = instantCache[key]
        return if (local != null) {
            target(local.value)
            null
        } else targetCache.getAsync(key) {
            if (get(key) == null) {
                instantCache[key] = it
                target(it)
            }
        }
    }

    override fun releaseKey(key: K) {
        if (releaseTargetCache) {
            targetCache.releaseKey(key)
        }
        super.releaseKey(key)
        instantCache.releaseKey(key)
    }

    override fun set(key: K, value: T) {
        instantCache[key] = value
        targetCache[key] = value
    }

}