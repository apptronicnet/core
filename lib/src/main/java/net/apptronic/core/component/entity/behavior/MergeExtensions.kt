package net.apptronic.core.component.entity.behavior

import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.functions.Function
import net.apptronic.core.component.entity.functions.predicateFunction

fun <R, A, B> merge(
    a: Predicate<A>,
    b: Predicate<B>,
    method: (A, B) -> R
): Function<R> {
    return predicateFunction(a, b, method)
}

fun <R, A, B, C> merge(
    a: Predicate<A>,
    b: Predicate<B>,
    c: Predicate<C>,
    method: (A, B, C) -> R
): Function<R> {
    return predicateFunction(a, b, c, method)
}

fun <R, A, B, C, D> merge(
    a: Predicate<A>,
    b: Predicate<B>,
    c: Predicate<C>,
    d: Predicate<D>,
    method: (A, B, C, D) -> R
): Function<R> {
    return predicateFunction(a, b, c, d, method)
}

fun <R, A, B, C, D, E> merge(
    a: Predicate<A>,
    b: Predicate<B>,
    c: Predicate<C>,
    d: Predicate<D>,
    e: Predicate<E>,
    method: (A, B, C, D, E) -> R
): Function<R> {
    return predicateFunction(a, b, c, d, e, method)
}