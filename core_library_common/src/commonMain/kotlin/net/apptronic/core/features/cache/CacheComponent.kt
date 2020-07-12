package net.apptronic.core.features.cache

import kotlinx.coroutines.Job
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.extensions.BaseComponent

abstract class CacheComponent<T, K>(context: Context) : BaseComponent(context) {

    abstract operator fun set(key: K, value: T)

    abstract fun get(key: K): ValueHolder<T>?

    open fun releaseKey(key: K) {
        // implement by subclasses if needed
    }

    open fun getAsync(key: K, target: (T) -> Unit): Job? {
        get(key)?.let {
            target(it.value)
        }
        return null
    }

}