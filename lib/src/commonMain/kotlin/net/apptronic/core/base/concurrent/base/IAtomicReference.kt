package net.apptronic.core.base.concurrent.base

/**
 * Reference which guarantees acces to same value for different threads
 */
interface IAtomicReference<T> {

    fun set(value: T)

    fun get(): T

}