package net.apptronic.common.core.mvvm.viewmodel.entity.functions.variants

internal infix fun <T : Number> T.plus(another: T): T {
    if (this is Byte) {
        return (toByte() + another.toByte()).toByte() as T
    }
    if (this is Short) {
        return (toShort() + another.toShort()).toShort() as T
    }
    if (this is Int) {
        return (toInt() + another.toInt()).toInt() as T
    }
    if (this is Long) {
        return (toLong() + another.toLong()).toLong() as T
    }
    if (this is Float) {
        return (toFloat() + another.toFloat()).toFloat() as T
    }
    if (this is Double) {
        return (toDouble() + another.toDouble()).toDouble() as T
    }
    throw IllegalArgumentException("${this::class}")
}

internal infix fun <T : Number> T.minus(another: T): T {
    if (this is Byte) {
        return (toByte() - another.toByte()).toByte() as T
    }
    if (this is Short) {
        return (toShort() - another.toShort()).toShort() as T
    }
    if (this is Int) {
        return (toInt() - another.toInt()).toInt() as T
    }
    if (this is Long) {
        return (toLong() - another.toLong()).toLong() as T
    }
    if (this is Float) {
        return (toFloat() - another.toFloat()).toFloat() as T
    }
    if (this is Double) {
        return (toDouble() - another.toDouble()).toDouble() as T
    }
    throw IllegalArgumentException("${this::class}")
}

internal infix fun <T : Number> T.mutliply(another: T): T {
    if (this is Byte) {
        return (toByte() * another.toByte()).toByte() as T
    }
    if (this is Short) {
        return (toShort() * another.toShort()).toShort() as T
    }
    if (this is Int) {
        return (toInt() * another.toInt()).toInt() as T
    }
    if (this is Long) {
        return (toLong() * another.toLong()).toLong() as T
    }
    if (this is Float) {
        return (toFloat() * another.toFloat()).toFloat() as T
    }
    if (this is Double) {
        return (toDouble() * another.toDouble()).toDouble() as T
    }
    throw IllegalArgumentException("${this::class}")
}

internal infix fun <T : Number> T.divide(another: T): T {
    if (this is Byte) {
        return (toByte() / another.toByte()).toByte() as T
    }
    if (this is Short) {
        return (toShort() / another.toShort()).toShort() as T
    }
    if (this is Int) {
        return (toInt() / another.toInt()).toInt() as T
    }
    if (this is Long) {
        return (toLong() / another.toLong()).toLong() as T
    }
    if (this is Float) {
        return (toFloat() / another.toFloat()).toFloat() as T
    }
    if (this is Double) {
        return (toDouble() / another.toDouble()).toDouble() as T
    }
    throw IllegalArgumentException("${this::class}")
}
