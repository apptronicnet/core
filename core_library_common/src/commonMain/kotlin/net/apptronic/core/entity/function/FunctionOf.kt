package net.apptronic.core.entity.function

import net.apptronic.core.context.Contextual
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.commons.BaseProperty
import net.apptronic.core.entity.commons.property

fun <T, A> Contextual.functionOf(
        a: Entity<A>,
        functionOf: (A) -> T
): BaseProperty<T> {
    return property(
            entityFunction(a, functionOf)
    )
}

fun <T, A, B> Contextual.functionOf(
        a: Entity<A>,
        b: Entity<B>,
        functionOf: (A, B) -> T
): BaseProperty<T> {
    return property(
            entityFunction(a, b, functionOf)
    )
}

fun <T, A, B, C> Contextual.functionOf(
        a: Entity<A>,
        b: Entity<B>,
        c: Entity<C>,
        functionOf: (A, B, C) -> T
): BaseProperty<T> {
    return property(
            entityFunction(a, b, c, functionOf)
    )
}

fun <T, A, B, C, D> Contextual.functionOf(
        a: Entity<A>,
        b: Entity<B>,
        c: Entity<C>,
        d: Entity<D>,
        functionOf: (A, B, C, D) -> T
): BaseProperty<T> {
    return property(
            entityFunction(a, b, c, d, functionOf)
    )
}

fun <T, A, B, C, D, E> Contextual.functionOf(
        a: Entity<A>,
        b: Entity<B>,
        c: Entity<C>,
        d: Entity<D>,
        e: Entity<E>,
        functionOf: (A, B, C, D, E) -> T
): BaseProperty<T> {
    return property(
            entityFunction(a, b, c, d, e, functionOf)
    )
}

fun <T> Contextual.functionOf(
        array: Array<Entity<*>>,
        functionOf: (Array<Any?>) -> T
): BaseProperty<T> {
    return property(
            entityArrayFunction(array, functionOf)
    )
}

