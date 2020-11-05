package net.apptronic.core.entity.function

import net.apptronic.core.entity.Entity

fun Entity<out Number>.toByte(): Entity<Byte> =
        entityFunction(this) {
            it.toByte()
        }

fun Entity<out Number>.toShort(): Entity<Short> =
        entityFunction(this) {
            it.toShort()
        }

fun Entity<out Number>.toInt(): Entity<Int> =
        entityFunction(this) {
            it.toInt()
        }

fun Entity<out Number>.toLong(): Entity<Long> =
        entityFunction(this) {
            it.toLong()
        }

fun Entity<out Number>.toFloat(): Entity<Float> =
        entityFunction(this) {
            it.toFloat()
        }

fun Entity<out Number>.toDouble(): Entity<Double> =
        entityFunction(this) {
            it.toDouble()
        }

operator fun <T : Number> Entity<T>.plus(another: Entity<T>): Entity<T> =
        entityFunction(this, another) { left, right ->
            left plus right
        }

operator fun <T : Number> Entity<T>.minus(another: Entity<T>): Entity<T> =
        entityFunction(this, another) { left, right ->
            left minus right
        }

operator fun <T : Number> Entity<T>.times(another: Entity<T>): Entity<T> =
        entityFunction(this, another) { left, right ->
            left mutliply right
        }

operator fun <T : Number> Entity<T>.div(another: Entity<T>): Entity<T> =
        entityFunction(this, another) { left, right ->
            left divide right
        }