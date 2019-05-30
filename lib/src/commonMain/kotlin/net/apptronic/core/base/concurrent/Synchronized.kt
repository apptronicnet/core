package net.apptronic.core.base.concurrent

import kotlinx.atomicfu.atomic
import net.apptronic.core.base.concurrent.base.ISynchronized

/**
 * Synchronization entity
 */
class Synchronized : ISynchronized {

    private val lock = atomic(false)

    /**
     * Run synchronized block
     */
    override fun <R> executeBlock(block: () -> R): R {
        return try {
            while (lock.value) {
                // repeat while locked
            }
            block.invoke()
        } finally {
            lock.value = false
        }
    }

}