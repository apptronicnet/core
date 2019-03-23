package net.apptronic.core.component.entity.functions.variants

import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.functions.predicateFunction

fun <T> Predicate<T>.isNull(): Predicate<Boolean> =
    predicateFunction(this) {
        it == null
    }

fun <T> Predicate<T>.isNotNull(): Predicate<Boolean> =
    predicateFunction(this) {
        it != null
    }

fun Predicate<Boolean>.isTrue(): Predicate<Boolean> =
    predicateFunction(this) {
        it
    }

fun Predicate<Boolean>.isFalse(): Predicate<Boolean> =
    predicateFunction(this) {
        it.not()
    }

fun Predicate<Boolean>.not(): Predicate<Boolean> =
    predicateFunction(this) {
        it.not()
    }

fun Predicate<Boolean?>.isTrueOrNull(): Predicate<Boolean> =
    predicateFunction(this) {
        it == null || it
    }

fun Predicate<Boolean?>.isFalseOrNull(): Predicate<Boolean> =
    predicateFunction(this) {
        it == null || it.not()
    }

infix fun <A, B> Predicate<A>.isEqualsTo(another: Predicate<B>): Predicate<Boolean> =
    predicateFunction(this, another) { left, right ->
        left == right
    }

infix fun <A, B> Predicate<A>.isEqualsTo(another: B): Predicate<Boolean> =
    predicateFunction(this) {
        it == another
    }

infix fun <A, B> Predicate<A>.isNotEqualsTo(another: Predicate<B>): Predicate<Boolean> =
    predicateFunction(this, another) { left, right ->
        left != right
    }

infix fun Predicate<Boolean>.and(another: Predicate<Boolean>): Predicate<Boolean> =
    predicateFunction(this, another) { left, right ->
        left and right
    }

infix fun Predicate<Boolean>.or(another: Predicate<Boolean>): Predicate<Boolean> =
    predicateFunction(this, another) { left, right ->
        left or right
    }

infix fun Predicate<Boolean>.xor(another: Predicate<Boolean>): Predicate<Boolean> =
    predicateFunction(this, another) { left, right ->
        left xor right
    }

fun <T : CharSequence> Predicate<T>.isEmpty(): Predicate<Boolean> =
    predicateFunction(this) {
        it.isEmpty()
    }

fun <T : CharSequence> Predicate<T>.isNoEmpty(): Predicate<Boolean> =
    predicateFunction(this) {
        it.isNotEmpty()
    }

fun <T : CharSequence> Predicate<T>.isNotBlank(): Predicate<Boolean> =
    predicateFunction(this) {
        it.isNotBlank()
    }