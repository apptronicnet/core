package net.apptronic.core.entity.commons

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual

fun <T> Contextual.mutableValue(): MutableValue<T> {
    return MutableValue(context)
}

fun <T> Contextual.mutableValue(defaultValue: T): MutableValue<T> {
    return mutableValue<T>().apply {
        set(defaultValue)
    }
}

class MutableValue<T> internal constructor(context: Context) : AbstractValue<T>(context) {

    override val observable: Observable<T> = subject

    fun update(action: (T) -> Unit) {
        subject.getValue()?.let {
            action.invoke(it.value)
            subject.update(it.value)
        }
    }

}

