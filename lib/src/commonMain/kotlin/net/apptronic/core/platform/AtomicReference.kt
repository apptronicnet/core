package net.apptronic.core.platform

/**
 * Reference which guarantees acces to same value for different threads
 */
interface AtomicReference<T> {

    fun set(value: T)

    fun get(): T

}