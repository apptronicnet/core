package net.apptronic.core.entity.entities

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.context.Context

class MutableValue<T>(context: Context) : AbstractValue<T>(context) {

    override val observable: Observable<T> = subject

    fun update(action: (T) -> Unit) {
        subject.getValue()?.let {
            action.invoke(it.value)
            subject.update(it.value)
        }
    }

}

