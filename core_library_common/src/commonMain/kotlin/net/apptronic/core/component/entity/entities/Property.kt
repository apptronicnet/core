package net.apptronic.core.component.entity.entities

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.distinctUntilChanged
import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.base.EntityValue
import net.apptronic.core.component.entity.base.ObservableEntity

abstract class Property<T>(override val context: Context) : ObservableEntity<T>(), EntityValue<T> {

    protected val subject = BehaviorSubject<T>()

    override val observable: Observable<T> = subject.distinctUntilChanged()

    override fun getValueHolder(): ValueHolder<T>? {
        return subject.getValue()
    }

    override fun toString(): String {
        val valueHolder = subject.getValue()
        return super.toString() + if (valueHolder == null) "/not-set" else "=$valueHolder"
    }

}