package net.apptronic.core.base.concurrent

import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import net.apptronic.core.base.concurrent.base.IAtomicReference

/**
 * Reference which guarantees acces to same value for different threads
 */
class AtomicReference<T>(initialValue: T) : IAtomicReference<T> {

    private val atomic: AtomicRef<T> = atomic<T>(initialValue)

    override fun set(value: T) {
        atomic.value = value
    }

    override fun get(): T {
        return atomic.value
    }

}