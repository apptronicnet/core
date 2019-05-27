package net.apptronic.core.base

import net.apptronic.core.platform.getPlatform

class AtomicEntity<T>(initialValue: T) {

    private val sync = getPlatform().createSynchronized()
    private val value = getPlatform().createAtomicReference(initialValue)

    fun set(value: T): T {
        return sync.run {
            this.value.set(value)
            value
        }
    }

    fun get(): T {
        return sync.run {
            value.get()
        }
    }

    fun <R> perform(block: AtomicEntity<T>.(T) -> R): R {
        return sync.run {
            block(value.get())
        }
    }

    override fun toString(): String {
        return "${this::class}/$value"
    }

}