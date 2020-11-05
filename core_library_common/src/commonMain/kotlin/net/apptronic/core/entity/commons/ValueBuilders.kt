package net.apptronic.core.entity.commons

import net.apptronic.core.context.Contextual
import net.apptronic.core.entity.Entity

fun <T> Contextual.value(): Value<T> {
    return Value(context)
}

fun <T> Contextual.value(defaultValue: T): Value<T> {
    return value<T>().apply {
        set(defaultValue)
    }
}

fun <T> Contextual.valueAs(source: Entity<T>): Value<T> {
    return value<T>().apply {
        setAs(source)
    }
}

fun <T> Contextual.valueSet() = mutableValue<MutableSet<T>>(mutableSetOf<T>())

fun <K, V> Contextual.valueMap() = mutableValue<MutableMap<K, V>>(mutableMapOf<K, V>())

fun <T> Contextual.valueList() = mutableValue<MutableList<T>>(mutableListOf<T>())

