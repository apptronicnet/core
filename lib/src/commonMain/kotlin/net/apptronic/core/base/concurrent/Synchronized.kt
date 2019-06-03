package net.apptronic.core.base.concurrent

import net.apptronic.core.base.concurrent.base.ISynchronized

/**
 * Synchronization entity
 */
class Synchronized : ISynchronized {

    private val lock = AtomicReference(false)

    /**
     * Run synchronized block
     */
    override fun <R> executeBlock(block: () -> R): R {
        return try {
            while (lock.get()) {
                // repeat while locked
            }
            lock.set(true)
            block.invoke()
        } finally {
            lock.set(false)
        }
    }

}