package net.apptronic.core.entity.association

/**
 * Interface which associates two values of different typed from one to another.
 *
 * For example it can be association between item and it's id.
 */
interface Association<T, E> {

    fun direct(value: T): E

    fun reverse(value: E): T

}