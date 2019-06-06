package net.apptronic.core.base.concurrent

import net.apptronic.core.base.concurrent.base.IAtomicEntity

class AtomicEntity<T>(initialValue: T) : IAtomicEntity<T> {

    private val sync = Synchronized()
    private val value = AtomicReference(initialValue)

    override fun set(value: T): T {
        return sync.executeBlock {
            this.value.set(value)
            value
        }
    }

    override fun get(): T {
        return sync.executeBlock {
            value.get()
        }
    }

    override fun <R> perform(block: AtomicEntity<T>.(T) -> R): R {
        return sync.executeBlock {
            block(value.get())
        }
    }

    override fun toString(): String {
        return "${this::class}/$value"
    }

}