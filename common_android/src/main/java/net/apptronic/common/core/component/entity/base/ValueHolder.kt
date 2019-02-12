package net.apptronic.common.core.component.entity.base

import net.apptronic.common.core.component.entity.entities.PropertyNotSetException

class ValueHolder<T>(val value: T) {

    override fun toString(): String {
        return "[$value]"
    }

}

fun <T> ValueHolder<T>?.getValue(): T {
    if (this != null) {
        return value
    } else {
        throw PropertyNotSetException()
    }
}