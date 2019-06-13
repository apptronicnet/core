package net.apptronic.core.base.concurrent

import net.apptronic.core.base.concurrent.base.IVolatile
import kotlin.native.concurrent.AtomicReference
import kotlin.native.concurrent.ensureNeverFrozen

actual class Volatile<T> actual constructor(initialValue: T) : IVolatile<T> {

    init {
        ensureNeverFrozen()
    }
    private var atomic = AtomicReference<T>(initialValue)

    override fun set(value: T) {
        atomic.value = value
    }

    override fun get(): T {
        return atomic.value
    }

}