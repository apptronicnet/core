package net.apptronic.core.base.concurrent

import net.apptronic.core.base.concurrent.base.IAtomicReference
import kotlin.native.concurrent.AtomicReference

actual class Volatile<T> actual constructor(initialValue: T) : IAtomicReference<T> {

    private val atomic = AtomicReference<T>(initialValue)

    override fun set(value: T) {
        atomic.value = value
    }

    override fun get(): T {
        return atomic.value
    }

}