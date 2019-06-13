package net.apptronic.core.base.concurrent

import net.apptronic.core.base.concurrent.base.IVolatile
import java.util.concurrent.atomic.AtomicReference

actual class Volatile<T> actual constructor(initialValue: T) : IVolatile<T> {

    private val atomic = AtomicReference<T>(initialValue)

    override fun set(value: T) {
        atomic.set(value)
    }

    override fun get(): T {
        return atomic.get()
    }

}