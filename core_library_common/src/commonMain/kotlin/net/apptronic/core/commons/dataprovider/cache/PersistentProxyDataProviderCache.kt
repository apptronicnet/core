package net.apptronic.core.commons.dataprovider.cache

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.apptronic.core.UnderDevelopment
import net.apptronic.core.base.subject.ValueHolder
import net.apptronic.core.context.Context
import net.apptronic.core.context.component.Component
import net.apptronic.core.context.coroutines.contextCoroutineScope

/**
 * Cache which stays on front of some another cache assuming that cache is "instant" meaning
 * caches values in-memory without background operations. In case when [mainCache] cannot return value then
 * value is get from [persistentCache] and all values set to both [mainCache] and [persistentCache].
 * [persistentCache] cache can be storage cache which takes longer to get/set but keeps it's data after
 * process restart or [mainCache] values are freed up.
 */
@UnderDevelopment
class PersistentProxyDataProviderCache<K, T>(
        context: Context,
        private val mainCache: DataProviderCache<K, T>,
        private val persistentCache: DataProviderPersistentCache<K, T>
) : Component(context), DataProviderCache<K, T> {

    override operator fun get(key: K): ValueHolder<T>? {
        return mainCache[key]
    }

    override fun getAsync(key: K, target: (T) -> Unit): Job? {
        val local = mainCache[key]
        return if (local != null) {
            target(local.value)
            null
        } else {
            contextCoroutineScope.launch {
                persistentCache.load(key)?.let {
                    mainCache[key] = it.value
                    target(it.value)
                }
            }
        }
    }

    override fun releaseKey(key: K) {
        mainCache.releaseKey(key)
    }

    override fun set(key: K, value: T) {
        mainCache[key] = value
        contextCoroutineScope.launch {
            persistentCache.save(key, value)
        }
    }

}