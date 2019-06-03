package net.apptronic.core.base.concurrent

import net.apptronic.core.base.concurrent.base.IAtomicReference
import java.util.concurrent.atomic.AtomicReference

actual class AtomicReference<T> actual constructor(initialValue: T) : IAtomicReference<T> {

    private val atomic = AtomicReference<T>(initialValue)

    override fun set(value: T) {
        atomic.set(value)
    }

    override fun get(): T {
        return atomic.get()
    }

}