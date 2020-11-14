package net.apptronic.core.entity.function

import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.Property

fun Entity<out Number>.toByte(): Property<Byte> =
        entityFunction(this) {
            it.toByte()
        }

fun Entity<out Number>.toShort(): Property<Short> =
        entityFunction(this) {
            it.toShort()
        }

fun Entity<out Number>.toInt(): Property<Int> =
        entityFunction(this) {
            it.toInt()
        }

fun Entity<out Number>.toLong(): Property<Long> =
        entityFunction(this) {
            it.toLong()
        }

fun Entity<out Number>.toFloat(): Property<Float> =
        entityFunction(this) {
            it.toFloat()
        }

fun Entity<out Number>.toDouble(): Property<Double> =
        entityFunction(this) {
            it.toDouble()
        }

operator fun <T : Number> Entity<T>.plus(another: Entity<T>): Property<T> =
        entityFunction(this, another) { left, right ->
            left plus right
        }

operator fun <T : Number> Entity<T>.minus(another: Entity<T>): Property<T> =
        entityFunction(this, another) { left, right ->
            left minus right
        }

operator fun <T : Number> Entity<T>.times(another: Entity<T>): Property<T> =
        entityFunction(this, another) { left, right ->
            left mutliply right
        }

operator fun <T : Number> Entity<T>.div(another: Entity<T>): Property<T> =
        entityFunction(this, another) { left, right ->
            left divide right
        }