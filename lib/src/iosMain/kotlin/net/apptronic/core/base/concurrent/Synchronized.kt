package net.apptronic.core.base.concurrent

import net.apptronic.core.base.concurrent.base.ISynchronized

actual class Synchronized : ISynchronized {

    private val lock = kotlin.native.concurrent.AtomicReference(false)

    override fun <R> executeBlock(block: () -> R): R {
        return try {
            while (!lock.compareAndSet(expected = false, new = true)) {
                // repeat while locked
            }
            block.invoke()
        } finally {
            while (!lock.compareAndSet(expected = true, new = false)) {
            }
        }
    }

}