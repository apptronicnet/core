package net.apptronic.core.base.concurrent

import kotlinx.atomicfu.AtomicRef
import kotlinx.atomicfu.atomic
import net.apptronic.core.base.concurrent.base.IAtomicReference

actual class AtomicReference<T> actual constructor(initialValue: T) : IAtomicReference<T> {

    private val atomic: AtomicRef<T> = atomic<T>(initialValue)

    override fun set(value: T) {
        atomic.value = value
    }

    override fun get(): T {
        return atomic.value
    }

}