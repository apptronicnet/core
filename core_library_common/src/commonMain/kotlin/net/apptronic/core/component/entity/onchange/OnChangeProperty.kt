package net.apptronic.core.component.entity.onchange

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.Contextual
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.base.EntityValue
import net.apptronic.core.component.entity.base.ObservableEntity
import net.apptronic.core.component.entity.entities.setAs
import net.apptronic.core.component.typedEvent
import net.apptronic.core.component.value

fun <T, E> Entity<Next<T, E>>.asOnChangeProperty(): OnChangeProperty<T, E> {
    return OnChangeValueImpl<T, E>(context).setAs(this)
}

fun <T, E> Contextual.onChangeProperty(source: Entity<Next<T, E>>): OnChangeProperty<T, E> {
    return onChangeValue<T, E>().setAs(source)
}

fun <T, E> Contextual.onChangeProperty(initValue: T): OnChangeProperty<T, E> {
    return onChangeValue(initValue)
}

fun <T, E> Contextual.onChangeValue(): OnChangeValue<T, E> {
    return OnChangeValueImpl<T, E>(context)
}

fun <T, E> Contextual.onChangeValue(initValue: T): OnChangeValue<T, E> {
    return OnChangeValueImpl<T, E>(context).apply {
        set(initValue)
    }
}

interface OnChangeProperty<T, E> : Entity<Next<T, E>>, EntityValue<Next<T, E>> {

    fun getValue(): T {
        return get().value
    }

    fun getValueOrNull(): T? {
        return getOrNull()?.value
    }

    fun doIfValueSet(action: (T) -> Unit): Boolean {
        return doIfSet {
            action.invoke(it.value)
        }
    }

    fun getValueOr(fallbackValue: T): T {
        val valueHolder = getValueHolder()
        return valueHolder?.value?.value ?: fallbackValue
    }

    fun getValueOr(fallbackValueProvider: () -> T): T {
        val valueHolder = getValueHolder()
        return valueHolder?.value?.value ?: fallbackValueProvider()
    }

}

/**
 * This variant of [Entity] designed to be property, which should pass additional information to observers when it's
 * changes, but not to store this information for new observers.
 */
abstract class OnChangePropertyImpl<T, E> internal constructor(
        final override val context: Context
) : ObservableEntity<Next<T, E>>(), OnChangeProperty<T, E> {

    internal val value = context.value<T>()
    internal var change: ValueHolder<E>? = null
    private val updateEvent = context.typedEvent<Next<T, E>>()

    override fun onObserverSubscribed(observer: Observer<Next<T, E>>) {
        super.onObserverSubscribed(observer)
        value.getValueHolder()?.let {
            observer.notify(Next(it.value, null))
        }
    }

    init {
        value.subscribe {
            updateEvent.sendEvent(Next(it, change?.value))
            change = null
        }
    }

    override val observable: Observable<Next<T, E>> = updateEvent

    override fun getValueHolder(): ValueHolder<Next<T, E>>? {
        return value.getValueHolder()?.let {
            ValueHolder<Next<T, E>>(Next(it.value, null))
        }
    }

}