package net.apptronic.core.component.entity.functions.variants

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.functions.entityFunction

fun Entity<Number>.toByte(): Entity<Byte> =
    entityFunction(this) {
        it.toByte()
    }

fun Entity<Number>.toShort(): Entity<Short> =
    entityFunction(this) {
        it.toShort()
    }

fun Entity<Number>.toInt(): Entity<Int> =
    entityFunction(this) {
        it.toInt()
    }

fun Entity<Number>.toLong(): Entity<Long> =
    entityFunction(this) {
        it.toLong()
    }

fun Entity<Number>.toFloat(): Entity<Float> =
    entityFunction(this) {
        it.toFloat()
    }

fun Entity<Number>.toDouble(): Entity<Double> =
    entityFunction(this) {
        it.toDouble()
    }

infix fun <T : Number> Entity<T>.plus(another: Entity<T>): Entity<T> =
    entityFunction(this, another) { left, right ->
        left plus right
    }

infix fun <T : Number> Entity<T>.minus(another: Entity<T>): Entity<T> =
    entityFunction(this, another) { left, right ->
        left minus right
    }

infix fun <T : Number> Entity<T>.mult(another: Entity<T>): Entity<T> =
    entityFunction(this, another) { left, right ->
        left mutliply right
    }

infix fun <T : Number> Entity<T>.div(another: Entity<T>): Entity<T> =
    entityFunction(this, another) { left, right ->
        left divide right
    }