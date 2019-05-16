package net.apptronic.core.component.entity.behavior

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.functions.Function
import net.apptronic.core.component.entity.functions.entityFunction

fun <R, A, B> merge(
    a: Entity<A>,
    b: Entity<B>,
    method: (A, B) -> R
): Function<R> {
    return entityFunction(a, b, method)
}

fun <R, A, B, C> merge(
    a: Entity<A>,
    b: Entity<B>,
    c: Entity<C>,
    method: (A, B, C) -> R
): Function<R> {
    return entityFunction(a, b, c, method)
}

fun <R, A, B, C, D> merge(
    a: Entity<A>,
    b: Entity<B>,
    c: Entity<C>,
    d: Entity<D>,
    method: (A, B, C, D) -> R
): Function<R> {
    return entityFunction(a, b, c, d, method)
}

fun <R, A, B, C, D, E> merge(
    a: Entity<A>,
    b: Entity<B>,
    c: Entity<C>,
    d: Entity<D>,
    e: Entity<E>,
    method: (A, B, C, D, E) -> R
): Function<R> {
    return entityFunction(a, b, c, d, e, method)
}