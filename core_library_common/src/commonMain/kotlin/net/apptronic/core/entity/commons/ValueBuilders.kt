package net.apptronic.core.entity.commons

import net.apptronic.core.base.utils.EqComparator
import net.apptronic.core.base.utils.NeverEqComparator
import net.apptronic.core.context.Contextual
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.MutableValue
import net.apptronic.core.entity.base.Value

fun <T> Contextual.value(): Value<T> {
    return SimpleValue(context)
}

fun <T> Contextual.value(defaultValue: T): Value<T> {
    return value<T>().apply {
        set(defaultValue)
    }
}

fun <T> Contextual.valueWithComparator(eqComparator: EqComparator<T>): Value<T> {
    return SimpleValue(context, eqComparator)
}

fun <T> Contextual.valueAs(source: Entity<T>): Value<T> {
    return value<T>().apply {
        setAs(source)
    }
}

fun <T> Contextual.mutableValue(): MutableValue<T> {
    return SimpleMutableValue(context)
}

fun <T> Contextual.mutableValue(defaultValue: T): MutableValue<T> {
    return mutableValue<T>().apply {
        set(defaultValue)
    }
}

fun <T> Contextual.mutableValue(source: Entity<T>): MutableValue<T> {
    return mutableValue<T>().apply {
        setAs(source)
    }
}

fun <T> Contextual.mutableValueWithComparator(eqComparator: EqComparator<T>): MutableValue<T> {
    return SimpleMutableValue(context, eqComparator)
}

fun <T> Contextual.valueSet() = valueMutator<MutableSet<T>>(mutableSetOf<T>())

fun <K, V> Contextual.valueMap() = valueMutator<MutableMap<K, V>>(mutableMapOf<K, V>())

fun <T> Contextual.valueList() = valueMutator<MutableList<T>>(mutableListOf<T>())

fun <T> Contextual.valueMutator(): Value<T> {
    return SimpleValue(context, NeverEqComparator())
}

fun <T> Contextual.valueMutator(defaultValue: T): Value<T> {
    return SimpleValue<T>(context, NeverEqComparator()).apply {
        set(defaultValue)
    }
}

