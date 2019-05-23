package net.apptronic.core.base

expect class AtomicEntity<T> {

    constructor(value: T)

    fun set(value: T): T

    fun get(): T

    fun <R> perform(block: AtomicEntity<T>.(T) -> R): R

    override fun toString(): String

}