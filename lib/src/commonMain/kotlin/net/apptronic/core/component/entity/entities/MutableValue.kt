package net.apptronic.core.component.entity.entities

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.component.context.Context

open class MutableValue<T>(context: Context) : Property<T>(context) {

    override fun getObservable(): Observable<T> {
        return subject
    }

    fun update(action: (T) -> Unit) {
        subject.getValue()?.let {
            action.invoke(it.value)
            subject.update(it.value)
        }
    }
}

