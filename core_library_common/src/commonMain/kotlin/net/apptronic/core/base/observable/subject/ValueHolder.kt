package net.apptronic.core.base.observable.subject

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.functions.map

class ValueHolder<T>(val value: T) {

    override fun toString(): String {
        return "[$value]"
    }

}

fun <T> T.asValueHolder(): ValueHolder<T> {
    return ValueHolder(this)
}

fun <T> Entity<T>.wrapValueHolder(): Entity<ValueHolder<T>> {
    return map { ValueHolder(it) }
}

fun <T> Entity<ValueHolder<T>>.unwrapValueHolder(): Entity<T> {
    return map { it.value }
}