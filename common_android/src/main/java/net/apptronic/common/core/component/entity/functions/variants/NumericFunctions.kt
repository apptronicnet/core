package net.apptronic.common.core.component.entity.functions.variants

import net.apptronic.common.core.component.entity.functions.Predicate
import net.apptronic.common.core.component.entity.functions.predicateFunction

fun Predicate<Number>.toByte(): Predicate<Byte> =
    predicateFunction(this) {
        it.toByte()
    }

fun Predicate<Number>.toShort(): Predicate<Short> =
    predicateFunction(this) {
        it.toShort()
    }

fun Predicate<Number>.toInt(): Predicate<Int> =
    predicateFunction(this) {
        it.toInt()
    }

fun Predicate<Number>.toLong(): Predicate<Long> =
    predicateFunction(this) {
        it.toLong()
    }

fun Predicate<Number>.toFloat(): Predicate<Float> =
    predicateFunction(this) {
        it.toFloat()
    }

fun Predicate<Number>.toDouble(): Predicate<Double> =
    predicateFunction(this) {
        it.toDouble()
    }

infix fun <T : Number> Predicate<T>.plus(another: Predicate<T>): Predicate<T> =
    predicateFunction(this, another) { left, right ->
        left plus right
    }

infix fun <T : Number> Predicate<T>.minus(another: Predicate<T>): Predicate<T> =
    predicateFunction(this, another) { left, right ->
        left minus right
    }

infix fun <T : Number> Predicate<T>.mult(another: Predicate<T>): Predicate<T> =
    predicateFunction(this, another) { left, right ->
        left mutliply right
    }

infix fun <T : Number> Predicate<T>.div(another: Predicate<T>): Predicate<T> =
    predicateFunction(this, another) { left, right ->
        left divide right
    }