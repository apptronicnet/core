package net.apptronic.core.entity.association

val <T, E> Association<T, E>.inverted: Association<E, T>
    get() = OppositeAssociation(this)

class OppositeAssociation<T, E> internal constructor(private val source: Association<E, T>) : Association<T, E> {

    override fun direct(value: T): E {
        return source.reverse(value)
    }

    override fun reverse(value: E): T {
        return source.direct(value)
    }

}