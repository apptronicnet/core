package net.apptronic.core.entity.reflection

val <T, E> Mirror<T, E>.inverted: Mirror<E, T>
    get() = OppositeMirror(this)

class OppositeMirror<T, E> internal constructor(private val source: Mirror<E, T>) : Mirror<T, E> {

    override fun direct(value: E): T {
        return source.reverse(value)
    }

    override fun reverse(value: T): E {
        return source.direct(value)
    }

}