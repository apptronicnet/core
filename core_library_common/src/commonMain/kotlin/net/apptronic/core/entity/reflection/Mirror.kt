package net.apptronic.core.entity.reflection

/**
 * Interface which mirrors two values of different typed from one to another.
 *
 * For example it can be mirroring between item and it's id.
 */
interface Mirror<T, E> {

    fun direct(value: E): T

    fun reverse(value: T): E

}