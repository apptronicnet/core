package net.apptronic.core.base.concurrent

import net.apptronic.core.base.concurrent.base.IVolatile

actual class Volatile<T> actual constructor(initialValue: T) : IVolatile<T> {

    @Volatile
    private var value: T = initialValue

    override fun set(value: T) {
        this.value = value
    }

    override fun get(): T {
        return value
    }

}