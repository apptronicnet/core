package net.apptronic.core.base

expect class AtomicReference<T>(value: T) {

    fun set(value: T)

    fun get(): T

}