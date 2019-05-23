package net.apptronic.core.platform

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