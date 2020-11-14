package net.apptronic.core.entity.function

import kotlinx.coroutines.CoroutineScope
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.Property

fun <R, A, B> merge(
        a: Entity<A>,
        b: Entity<B>,
        method: (A, B) -> R
): Property<R> {
    return entityFunction(a, b, method)
}

fun <R, A, B> mergeSuspend(
        a: Entity<A>,
        b: Entity<B>,
        method: suspend CoroutineScope.(A, B) -> R
): Property<R> {
    return entityFunctionSuspend(a, b, method)
}

fun <R, A, B, C> merge(
        a: Entity<A>,
        b: Entity<B>,
        c: Entity<C>,
        method: (A, B, C) -> R
): Property<R> {
    return entityFunction(a, b, c, method)
}

fun <R, A, B, C> mergeSuspend(
        a: Entity<A>,
        b: Entity<B>,
        c: Entity<C>,
        method: suspend CoroutineScope.(A, B, C) -> R
): Property<R> {
    return entityFunctionSuspend(a, b, c, method)
}

fun <R, A, B, C, D> merge(
        a: Entity<A>,
        b: Entity<B>,
        c: Entity<C>,
        d: Entity<D>,
        method: (A, B, C, D) -> R
): Property<R> {
    return entityFunction(a, b, c, d, method)
}

fun <R, A, B, C, D> mergeSuspend(
        a: Entity<A>,
        b: Entity<B>,
        c: Entity<C>,
        d: Entity<D>,
        method: suspend CoroutineScope.(A, B, C, D) -> R
): Property<R> {
    return entityFunctionSuspend(a, b, c, d, method)
}

fun <R, A, B, C, D, E> merge(
        a: Entity<A>,
        b: Entity<B>,
        c: Entity<C>,
        d: Entity<D>,
        e: Entity<E>,
        method: (A, B, C, D, E) -> R
): Property<R> {
    return entityFunction(a, b, c, d, e, method)
}

fun <R, A, B, C, D, E> mergeSuspend(
        a: Entity<A>,
        b: Entity<B>,
        c: Entity<C>,
        d: Entity<D>,
        e: Entity<E>,
        method: suspend CoroutineScope.(A, B, C, D, E) -> R
): Property<R> {
    return entityFunctionSuspend(a, b, c, d, e, method)
}

@Suppress("UNCHECKED_CAST")
fun <R, T> mergeArray(
        vararg array: Entity<out T>,
        method: (List<T>) -> R
): Property<R> {
    val sources = array.map { it as Entity<*> }.toTypedArray()
    val function: (Array<*>) -> R = {
        val list = it.map { it as T }.toList()
        method(list)
    }
    return entityArrayFunction(sources, function)
}

@Suppress("UNCHECKED_CAST")
fun <R, T> mergeArraySuspend(
        vararg array: Entity<out T>,
        method: suspend CoroutineScope.(List<T>) -> R
): Property<R> {
    val sources = array.map { it as Entity<*> }.toTypedArray()
    val function: suspend CoroutineScope.(Array<*>) -> R = {
        val list = it.map { it as T }.toList()
        method(list)
    }
    return entityArrayFunctionSuspend(sources, function)
}

fun <R, A, B> Entity<A>.mergeWith(
        b: Entity<B>,
        method: (A, B) -> R
): Property<R> {
    return merge(this, b, method)
}

fun <R, A, B> Entity<A>.mergeSuspendWith(
        b: Entity<B>,
        method: suspend CoroutineScope.(A, B) -> R
): Property<R> {
    return mergeSuspend(this, b, method)
}

fun <R, A, B, C> Entity<A>.mergeWith(
        b: Entity<B>,
        c: Entity<C>,
        method: (A, B, C) -> R
): Property<R> {
    return merge(this, b, c, method)
}

fun <R, A, B, C> Entity<A>.mergeSuspendWith(
        b: Entity<B>,
        c: Entity<C>,
        method: suspend CoroutineScope.(A, B, C) -> R
): Property<R> {
    return mergeSuspend(this, b, c, method)
}

fun <R, A, B, C, D> Entity<A>.mergeWith(
        b: Entity<B>,
        c: Entity<C>,
        d: Entity<D>,
        method: (A, B, C, D) -> R
): Property<R> {
    return merge(this, b, c, d, method)
}

fun <R, A, B, C, D> Entity<A>.mergeSuspendWith(
        b: Entity<B>,
        c: Entity<C>,
        d: Entity<D>,
        method: suspend CoroutineScope.(A, B, C, D) -> R
): Property<R> {
    return mergeSuspend(this, b, c, d, method)
}

fun <R, A, B, C, D, E> Entity<A>.mergeWith(
        b: Entity<B>,
        c: Entity<C>,
        d: Entity<D>,
        e: Entity<E>,
        method: (A, B, C, D, E) -> R
): Property<R> {
    return merge(this, b, c, d, e, method)
}

fun <R, A, B, C, D, E> Entity<A>.mergeSuspendWith(
        b: Entity<B>,
        c: Entity<C>,
        d: Entity<D>,
        e: Entity<E>,
        method: suspend CoroutineScope.(A, B, C, D, E) -> R
): Property<R> {
    return mergeSuspend(this, b, c, d, e, method)
}

fun <R, S, T> Entity<S>.mergeWithArray(
        vararg array: Entity<out T>,
        method: (S, List<T>) -> R
): Property<R> {
    val listEntity = mergeArray(*array) {
        it
    }
    return merge(this, listEntity, method)
}

fun <R, S, T> Entity<S>.mergeSuspendWithArray(
        vararg array: Entity<out T>,
        method: suspend CoroutineScope.(S, List<T>) -> R
): Property<R> {
    val listEntity = mergeArray(*array) {
        it
    }
    return mergeSuspend(this, listEntity, method)
}

class MergeTwoResult<A, B>(val first: A, val second: B)
class MergeThreeResult<A, B, C>(val first: A, val second: B, val third: C)
class MergeFourResult<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
class MergeFiveResult<A, B, C, D, E>(val first: A, val second: B, val third: C, val fourth: D, val fifth: E)
class MergeArrayResult<S, T>(val value: S, val list: List<T>)

fun <A, B> merge(
        a: Entity<A>,
        b: Entity<B>
): Property<MergeTwoResult<A, B>> {
    return merge(a, b) { first, second ->
        MergeTwoResult(first, second)
    }
}

fun <A, B, C> merge(
        a: Entity<A>,
        b: Entity<B>,
        c: Entity<C>
): Property<MergeThreeResult<A, B, C>> {
    return merge(a, b, c) { first, second, third ->
        MergeThreeResult(first, second, third)
    }
}

fun <A, B, C, D> merge(
        a: Entity<A>,
        b: Entity<B>,
        c: Entity<C>,
        d: Entity<D>
): Property<MergeFourResult<A, B, C, D>> {
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
): Property<MergeFiveResult<A, B, C, D, E>> {
    return merge(a, b, c, d, e) { first, second, third, fourth, fifth ->
        MergeFiveResult(first, second, third, fourth, fifth)
    }
}

fun <T> mergeArray(
        vararg array: Entity<out T>
): Property<List<T>> {
    return mergeArray(*array) { it }
}

fun <A, B> Entity<A>.mergeWith(
        b: Entity<B>
): Property<MergeTwoResult<A, B>> {
    return merge(this, b) { first, second ->
        MergeTwoResult(first, second)
    }
}

fun <A, B, C> Entity<A>.mergeWith(
        b: Entity<B>,
        c: Entity<C>
): Property<MergeThreeResult<A, B, C>> {
    return merge(this, b, c) { first, second, third ->
        MergeThreeResult(first, second, third)
    }
}

fun <A, B, C, D> Entity<A>.mergeWith(
        b: Entity<B>,
        c: Entity<C>,
        d: Entity<D>
): Property<MergeFourResult<A, B, C, D>> {
    return merge(this, b, c, d) { first, second, third, fourth ->
        MergeFourResult(first, second, third, fourth)
    }
}

fun <A, B, C, D, E> Entity<A>.mergeWith(
        b: Entity<B>,
        c: Entity<C>,
        d: Entity<D>,
        e: Entity<E>
): Property<MergeFiveResult<A, B, C, D, E>> {
    return merge(this, b, c, d, e) { first, second, third, fourth, fifth ->
        MergeFiveResult(first, second, third, fourth, fifth)
    }
}

fun <S, T> Entity<S>.mergeWithArray(
        vararg array: Entity<out T>
): Property<MergeArrayResult<S, T>> {
    return mergeWithArray(*array) { value, list ->
        MergeArrayResult(value, list)
    }
}