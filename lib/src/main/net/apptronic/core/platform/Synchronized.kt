package net.apptronic.core.platform

/**
 * Synchronization entity
 */
expect class Synchronized() {

    /**
     * Run synchronized block
     */
    fun <R> run(block: () -> R): R

}