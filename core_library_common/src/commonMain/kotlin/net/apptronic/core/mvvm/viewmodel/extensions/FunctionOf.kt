package net.apptronic.core.mvvm.viewmodel.extensions

import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.entities.Property
import net.apptronic.core.component.entity.functions.entityArrayFunction
import net.apptronic.core.component.entity.functions.entityFunction
import net.apptronic.core.component.property

fun <T, A> Contextual.functionOf(
        a: Entity<A>,
        functionOf: (A) -> T
): Property<T> {
    return property(
            entityFunction(a, functionOf)
    )
}

fun <T, A, B> Contextual.functionOf(
        a: Entity<A>,
        b: Entity<B>,
        functionOf: (A, B) -> T
): Property<T> {
    return property(
            entityFunction(a, b, functionOf)
    )
}

fun <T, A, B, C> Contextual.functionOf(
        a: Entity<A>,
        b: Entity<B>,
        c: Entity<C>,
        functionOf: (A, B, C) -> T
): Property<T> {
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
): Property<T> {
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
): Property<T> {
    return property(
            entityFunction(a, b, c, d, e, functionOf)
    )
}

fun <T> Contextual.functionOf(
        array: Array<Entity<*>>,
        functionOf: (Array<Any?>) -> T
): Property<T> {
    return property(
            entityArrayFunction(array, functionOf)
    )
}

