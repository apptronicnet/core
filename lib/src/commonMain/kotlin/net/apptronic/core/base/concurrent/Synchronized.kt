package net.apptronic.core.base.concurrent

import kotlinx.atomicfu.atomic

/**
 * Synchronization entity
 */
class Synchronized {

    private val lock = atomic(false)

    /**
     * Run synchronized block
     */
    fun <R> run(block: () -> R): R {
        return try {
            while (lock.value) {
            }
            block.invoke()
        } finally {
            lock.value = false
        }
    }

}