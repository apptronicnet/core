package net.apptronic.core.base.concurrent

import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic

/**
 * Reference which guarantees acces to same value for different threads
 */
class AtomicReference<T>(initialValue: T) {

    private val atomic: AtomicRef<T> = atomic<T>(initialValue)

    fun set(value: T) {
        atomic.value = value
    }

    fun get(): T {
        return atomic.value
    }

}