package net.apptronic.core.base.concurrent.base

import net.apptronic.core.base.concurrent.AtomicEntity

interface IAtomicEntity<T> {

    fun set(value: T): T

    fun get(): T

    fun <R> perform(block: AtomicEntity<T>.(T) -> R): R

}