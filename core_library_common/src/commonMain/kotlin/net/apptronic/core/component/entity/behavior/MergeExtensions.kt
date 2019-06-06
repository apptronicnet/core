package net.apptronic.core.component.entity.behavior

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.functions.Function
import net.apptronic.core.component.entity.functions.entityArrayFunction
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

fun <R, T> mergeArray(
        vararg array: Entity<out T>,
        method: (List<T>) -> R
): Function<R> {
    val sources = array.map { it as Entity<*> }.toTypedArray()
    return entityArrayFunction(sources) {
        val list = it.map { it as T }.toList()
        method.invoke(list)
    }
}

fun <R, A, B> Entity<A>.mergeWith(
        b: Entity<B>,
        method: (A, B) -> R
): Function<R> {
    return merge(this, b, method)
}

fun <R, A, B, C> Entity<A>.mergeWith(
        b: Entity<B>,
        c: Entity<C>,
        method: (A, B, C) -> R
): Function<R> {
    return merge(this, b, c, method)
}

fun <R, A, B, C, D> Entity<A>.mergeWith(
        b: Entity<B>,
        c: Entity<C>,
        d: Entity<D>,
        method: (A, B, C, D) -> R
): Function<R> {
    return merge(this, b, c, d, method)
}

fun <R, A, B, C, D, E> Entity<A>.mergeWith(
        b: Entity<B>,
        c: Entity<C>,
        d: Entity<D>,
        e: Entity<E>,
        method: (A, B, C, D, E) -> R
): Function<R> {
    return merge(this, b, c, d, e, method)
}


fun <R, S, T> Entity<S>.mergeWithArray(
        vararg array: Entity<out T>,
        method: (S, List<T>) -> R
): Function<R> {
    val listEntity = mergeArray(*array) {
        it
    }
    return merge(this, listEntity, method)
}

class MergeTwoResult<A, B>(val first: A, val second: B)
class MergeThreeResult<A, B, C>(val first: A, val second: B, val third: C)
class MergeFourResult<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
class MergeFiveResult<A, B, C, D, E>(val first: A, val second: B, val third: C, val fourth: D, val fifth: E)
class MergeArrayResult<S, T>(val value: S, val list: List<T>)

fun <A, B> merge(
        a: Entity<A>,
        b: Entity<B>
): Function<MergeTwoResult<A, B>> {
    return merge(a, b) { first, second ->
        MergeTwoResult(first, second)
    }
}

fun <A, B, C> merge(
        a: Entity<A>,
        b: Entity<B>,
        c: Entity<C>
): Function<MergeThreeResult<A, B, C>> {
    return merge(a, b, c) { first, second, third ->
        MergeThreeResult(first, second, third)
    }
}

fun <A, B, C, D> merge(
        a: Entity<A>,
        b: Entity<B>,
        c: Entity<C>,
        d: Entity<D>
): Function<MergeFourResult<A, B, C, D>> {
    return merge(a, b, c, d) { first, second, third, fourth ->
        MergeFourResult(first, second, third, fourth)
    }
}

fun <A, B, C, D, E> merge(
        a: Entity<A>,
        b: Entity<B>,
        c: Entity<C>,
        d: Entity<D>,
        e: Entity<E>
): Function<MergeFiveResult<A, B, C, D, E>> {
    return merge(a, b, c, d, e) { first, second, third, fourth, fifth ->
        MergeFiveResult(first, second, third, fourth, fifth)
    }
}

fun <T> mergeArray(
        vararg array: Entity<out T>
): Function<List<T>> {
    return mergeArray(*array) { it }
}

fun <A, B> Entity<A>.mergeWith(
        b: Entity<B>
): Function<MergeTwoResult<A, B>> {
    return merge(this, b) { first, second ->
        MergeTwoResult(first, second)
    }
}

fun <A, B, C> Entity<A>.mergeWith(
        b: Entity<B>,
        c: Entity<C>
): Function<MergeThreeResult<A, B, C>> {
    return merge(this, b, c) { first, second, third ->
        MergeThreeResult(first, second, third)
    }
}

fun <A, B, C, D> Entity<A>.mergeWith(
        b: Entity<B>,
        c: Entity<C>,
        d: Entity<D>
): Function<MergeFourResult<A, B, C, D>> {
    return merge(this, b, c, d) { first, second, third, fourth ->
        MergeFourResult(first, second, third, fourth)
    }
}

fun <A, B, C, D, E> Entity<A>.mergeWith(
        b: Entity<B>,
        c: Entity<C>,
        d: Entity<D>,
        e: Entity<E>
): Function<MergeFiveResult<A, B, C, D, E>> {
    return merge(this, b, c, d, e) { first, second, third, fourth, fifth ->
        MergeFiveResult(first, second, third, fourth, fifth)
    }
}

fun <S, T> Entity<S>.mergeWithArray(
        vararg array: Entity<out T>
): Function<MergeArrayResult<S, T>> {
    return mergeWithArray(*array) { value, list ->
        MergeArrayResult(value, list)
    }
}