package net.apptronic.core.base

actual class AtomicReference<T> actual constructor(
        @Volatile
        private var value: T
) {

    actual fun set(value: T) {
        this.value = value
    }

    actual fun get(): T {
        return value
    }

}