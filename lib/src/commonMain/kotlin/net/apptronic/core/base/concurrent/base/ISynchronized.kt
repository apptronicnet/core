package net.apptronic.core.base.concurrent.base

/**
 * Synchronization entity
 */
interface ISynchronized {

    /**
     * Run synchronized block
     */
    fun <R> executeBlock(block: () -> R): R

}