package net.apptronic.common.android.ui.viewmodel.entity.functions.variants

import net.apptronic.common.android.ui.viewmodel.entity.functions.Predicate
import net.apptronic.common.android.ui.viewmodel.entity.functions.predicateFunction

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
        if (left is Byte) {
            return@predicateFunction (left.toByte() + right.toByte()) as T
        }
        if (left is Short) {
            return@predicateFunction (left.toShort() + right.toShort()) as T
        }
        if (left is Int) {
            return@predicateFunction (left.toInt() + right.toInt()) as T
        }
        if (left is Long) {
            return@predicateFunction (left.toLong() + right.toLong()) as T
        }
        if (left is Float) {
            return@predicateFunction (left.toFloat() + right.toFloat()) as T
        }
        if (left is Double) {
            return@predicateFunction (left.toDouble() + right.toDouble()) as T
        }
        throw IllegalArgumentException("${left::class}")
    }

infix fun <T : Number> Predicate<T>.minus(another: Predicate<T>): Predicate<T> =
    predicateFunction(this, another) { left, right ->
        if (left is Byte) {
            return@predicateFunction (left.toByte() - right.toByte()) as T
        }
        if (left is Short) {
            return@predicateFunction (left.toShort() - right.toShort()) as T
        }
        if (left is Int) {
            return@predicateFunction (left.toInt() - right.toInt()) as T
        }
        if (left is Long) {
            return@predicateFunction (left.toLong() - right.toLong()) as T
        }
        if (left is Float) {
            return@predicateFunction (left.toFloat() - right.toFloat()) as T
        }
        if (left is Double) {
            return@predicateFunction (left.toDouble() - right.toDouble()) as T
        }
        throw IllegalArgumentException("${left::class}")
    }

infix fun <T : Number> Predicate<T>.mult(another: Predicate<T>): Predicate<T> =
    predicateFunction(this, another) { left, right ->
        if (left is Byte) {
            return@predicateFunction (left.toByte() * right.toByte()) as T
        }
        if (left is Short) {
            return@predicateFunction (left.toShort() * right.toShort()) as T
        }
        if (left is Int) {
            return@predicateFunction (left.toInt() * right.toInt()) as T
        }
        if (left is Long) {
            return@predicateFunction (left.toLong() * right.toLong()) as T
        }
        if (left is Float) {
            return@predicateFunction (left.toFloat() * right.toFloat()) as T
        }
        if (left is Double) {
            return@predicateFunction (left.toDouble() * right.toDouble()) as T
        }
        throw IllegalArgumentException("${left::class}")
    }

infix fun <T : Number> Predicate<T>.div(another: Predicate<T>): Predicate<T> =
    predicateFunction(this, another) { left, right ->
        if (left is Byte) {
            return@predicateFunction (left.toByte() / right.toByte()) as T
        }
        if (left is Short) {
            return@predicateFunction (left.toShort() / right.toShort()) as T
        }
        if (left is Int) {
            return@predicateFunction (left.toInt() / right.toInt()) as T
        }
        if (left is Long) {
            return@predicateFunction (left.toLong() / right.toLong()) as T
        }
        if (left is Float) {
            return@predicateFunction (left.toFloat() / right.toFloat()) as T
        }
        if (left is Double) {
            return@predicateFunction (left.toDouble() / right.toDouble()) as T
        }
        throw IllegalArgumentException("${left::class}")
    }