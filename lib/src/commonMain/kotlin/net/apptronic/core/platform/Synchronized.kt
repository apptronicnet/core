package net.apptronic.core.platform

/**
 * Synchronization entity
 */
interface Synchronized {

    /**
     * Run synchronized block
     */
    fun <R> run(block: () -> R): R

}