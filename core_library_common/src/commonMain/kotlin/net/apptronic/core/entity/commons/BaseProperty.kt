package net.apptronic.core.entity.commons

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.distinctUntilChanged
import net.apptronic.core.base.subject.*
import net.apptronic.core.base.utils.EqComparator
import net.apptronic.core.base.utils.SimpleEqComparator
import net.apptronic.core.context.Context
import net.apptronic.core.entity.base.ObservableEntity
import net.apptronic.core.entity.base.Property

abstract class BaseProperty<T>(
    override val context: Context,
    private val eqComparator: EqComparator<T> = SimpleEqComparator<T>()
) : ObservableEntity<T>(), Property<T> {

    protected val subject = BehaviorSubject<T>()
    override val observable: Observable<T> = subject.distinctUntilChanged(eqComparator)

    final override fun getValueHolder(): ValueHolder<T>? {
        return subject.getValue()
    }

    final override fun get() = getValueHolder().getOrThrow()

    final override fun getOrNull() = getValueHolder().getOrNull()

    final override fun getOr(fallbackValue: T) = getValueHolder().getOr(fallbackValue)

    final override fun getOr(fallbackValueProvider: () -> T) = getValueHolder().getOr(fallbackValueProvider)

    final override fun isSet() = getValueHolder().isSet()

    final override fun isNotSet() = !getValueHolder().isSet()

    final override fun doIfSet(action: (T) -> Unit) = getValueHolder().doIfSet(action)

    override fun toString(): String {
        val valueHolder = subject.getValue()
        return super.toString() + if (valueHolder == null) "/not-set" else "=$valueHolder"
    }

}