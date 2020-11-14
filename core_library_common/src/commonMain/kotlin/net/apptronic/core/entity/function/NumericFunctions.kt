package net.apptronic.core.entity.function

import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.FunctionProperty

fun Entity<out Number>.toByte(): FunctionProperty<Byte> =
        entityFunction(this) {
            it.toByte()
        }

fun Entity<out Number>.toShort(): FunctionProperty<Short> =
        entityFunction(this) {
            it.toShort()
        }

fun Entity<out Number>.toInt(): FunctionProperty<Int> =
        entityFunction(this) {
            it.toInt()
        }

fun Entity<out Number>.toLong(): FunctionProperty<Long> =
        entityFunction(this) {
            it.toLong()
        }

fun Entity<out Number>.toFloat(): FunctionProperty<Float> =
        entityFunction(this) {
            it.toFloat()
        }

fun Entity<out Number>.toDouble(): FunctionProperty<Double> =
        entityFunction(this) {
            it.toDouble()
        }

operator fun <T : Number> Entity<T>.plus(another: Entity<T>): FunctionProperty<T> =
        entityFunction(this, another) { left, right ->
            left plus right
        }

operator fun <T : Number> Entity<T>.minus(another: Entity<T>): FunctionProperty<T> =
        entityFunction(this, another) { left, right ->
            left minus right
        }

operator fun <T : Number> Entity<T>.times(another: Entity<T>): FunctionProperty<T> =
        entityFunction(this, another) { left, right ->
            left mutliply right
        }

operator fun <T : Number> Entity<T>.div(another: Entity<T>): FunctionProperty<T> =
        entityFunction(this, another) { left, right ->
            left divide right
        }