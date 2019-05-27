package net.apptronic.core.android.platform

import net.apptronic.core.platform.AtomicReference

class JavaAtomicReference<T> constructor(
        @Volatile
        private var value: T
) : AtomicReference<T> {

    override fun set(value: T) {
        this.value = value
    }

    override fun get(): T {
        return value
    }

}