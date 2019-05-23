package net.apptronic.core.base

actual class AtomicEntity<T> {

    @Volatile
    private var value: T

    actual constructor(value: T) {
        this.value = value
    }

    actual fun set(value: T): T {
        synchronized(this) {
            this.value = value
            return value
        }
    }

    actual fun get(): T {
        synchronized(this) {
            return value
        }
    }

    actual fun <R> perform(block: AtomicEntity<T>.(T) -> R): R {
        synchronized(this) {
            return block(value)
        }
    }

    actual override fun toString(): String {
        return "${this::class}/$value"
    }

}