package net.apptronic.common.core.base

class AtomicEntity<T> {

    @Volatile
    private var value: T

    constructor(value: T) {
        this.value = value
    }

    fun set(value: T): T {
        synchronized(this) {
            this.value = value
            return value
        }
    }

    fun get(): T {
        synchronized(this) {
            return value
        }
    }

    fun <R> perform(block: AtomicEntity<T>.(T) -> R): R {
        synchronized(this) {
            return block(value)
        }
    }

    override fun toString(): String {
        return "${this::class}/$value"
    }

}