package net.apptronic.core.commons.dataprovider.cache

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.apptronic.core.base.subject.ValueHolder
import net.apptronic.core.context.Context
import net.apptronic.core.context.component.Component
import net.apptronic.core.context.coroutines.contextCoroutineScope

class PersistentDataProviderCache<K, T>(
    context: Context,
    private val persistence: CachePersistence<K, T>
) : Component(context), DataProviderCache<K, T> {

    override fun set(key: K, value: T) {
        contextCoroutineScope.launch {
            persistence.save(key, value)
        }
    }

    override fun get(key: K): ValueHolder<T>? {
        // persistent data cannot be loaded in main thread
        return null
    }

    override fun getAsync(key: K, target: (T) -> Unit): Job? {
        return contextCoroutineScope.launch {
            persistence.load(key)?.also {
                target(it.value)
            }
        }
    }

    override fun releaseKey(key: K) {
        // persistent cache does not have state which is needed to release
    }

}