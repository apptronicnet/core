package net.apptronic.core.platform

/**
 * Reference which guarantees acces to same value for different threads
 */
expect class AtomicReference<T>(value: T) {

    fun set(value: T)

    fun get(): T

}