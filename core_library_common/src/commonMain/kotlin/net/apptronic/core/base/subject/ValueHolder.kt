package net.apptronic.core.base.subject

import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.ValueNotSetException
import net.apptronic.core.entity.function.map

data class ValueHolder<T>(val value: T) {

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


fun <T> ValueHolder<T>?.get(): T {
    if (this != null) {
        return this.value
    } else {
        throw ValueNotSetException()
    }
}


fun <T> ValueHolder<T>?.getOrNull(): T? {
    return this?.value
}

fun <T> ValueHolder<T>?.isSet(): Boolean {
    return this != null
}

fun <T> ValueHolder<T>?.doIfSet(action: (T) -> Unit): Boolean {
    return this?.let {
        action.invoke(it.value)
        true
    } ?: false
}