package net.apptronic.core.base.concurrent

import net.apptronic.core.base.concurrent.base.IVolatile
import kotlin.native.concurrent.AtomicReference

actual class Volatile<T> actual constructor(initialValue: T) : IVolatile<T> {

    private var atomic = AtomicReference<T>(initialValue)

    override fun set(value: T) {
        atomic.value = value
    }

    override fun get(): T {
        return atomic.value
    }

}