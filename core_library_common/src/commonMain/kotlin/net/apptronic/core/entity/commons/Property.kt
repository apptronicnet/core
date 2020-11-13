package net.apptronic.core.entity.commons

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.distinctUntilChanged
import net.apptronic.core.base.subject.BehaviorSubject
import net.apptronic.core.base.subject.ValueHolder
import net.apptronic.core.base.utils.EqComparator
import net.apptronic.core.base.utils.SimpleEqComparator
import net.apptronic.core.context.Context
import net.apptronic.core.entity.base.EntityValue
import net.apptronic.core.entity.base.ObservableEntity

abstract class Property<T>(
        override val context: Context,
        private val eqComparator: EqComparator<T> = SimpleEqComparator<T>()
) : ObservableEntity<T>(), EntityValue<T> {

    protected val subject = BehaviorSubject<T>()

    final override val observable: Observable<T> = subject.distinctUntilChanged(eqComparator)

    final override fun getValueHolder(): ValueHolder<T>? {
        return subject.getValue()
    }

    override fun toString(): String {
        val valueHolder = subject.getValue()
        return super.toString() + if (valueHolder == null) "/not-set" else "=$valueHolder"
    }

}