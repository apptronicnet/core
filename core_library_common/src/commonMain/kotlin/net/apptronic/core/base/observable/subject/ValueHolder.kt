package net.apptronic.core.base.observable.subject

class ValueHolder<T>(val value: T) {

    override fun toString(): String {
        return "[$value]"
    }

}