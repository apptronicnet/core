package net.apptronic.core.entity.onchange

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.subject.*
import net.apptronic.core.context.Context
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.ObservableEntity
import net.apptronic.core.entity.base.Value
import net.apptronic.core.entity.commons.reflect
import net.apptronic.core.entity.commons.typedEvent
import net.apptronic.core.entity.commons.value

/**
 * This variant of [Entity] designed to be property, which should pass additional information to observers when it's
 * changes, but not to store this information for new observers.
 */
internal class OnChangeImpl<T, E>(
        override val context: Context,
) : ObservableEntity<Next<T, E>>(), OnChangeValue<T, E> {

    private val value: Value<Next<T, E>> = context.value()
    private val reflection = value.reflect(direct = { it.value }, reverse = { Next(it, null) })
    private val updateEvent = context.typedEvent<Next<T, E>>()
    override val observable: Observable<Next<T, E>> = updateEvent

    override fun set(next: T, change: E?) {
        update(Next(next, change))
    }

    override fun update(next: T, change: E?) {
        update(Next(next, change))
    }

    override fun getValueHolder(): ValueHolder<Next<T, E>>? {
        return value.getValueHolder()?.let {
            ValueHolder<Next<T, E>>(Next(it.value.value, null))
        }
    }

    override fun set(value: Next<T, E>) {
        updateEvent.update(value)
        this.value.set(Next(value.value, null))
    }

    override fun update(value: Next<T, E>) {
        updateEvent.update(value)
        this.value.update(Next(value.value, null))
    }

    override fun onObserverSubscribed(observer: Observer<Next<T, E>>) {
        super.onObserverSubscribed(observer)
        value.getValueHolder()?.let {
            observer.update(it.value)
        }
    }

    override fun updateValue(updateCall: (Next<T, E>) -> Next<T, E>) {
        val current = get()
        val next = updateCall(current)
        update(next)
    }

    override fun getValue(): T {
        return get().value
    }

    override fun getValueOrNull(): T? {
        return getOrNull()?.value
    }

    override fun doIfValueSet(action: (T) -> Unit): Boolean {
        return doIfSet {
            action.invoke(it.value)
        }
    }

    override fun getValueOr(fallbackValue: T): T {
        val valueHolder = getValueHolder()
        return valueHolder?.value?.value ?: fallbackValue
    }

    override fun getValueOr(fallbackValueProvider: () -> T): T {
        val valueHolder = getValueHolder()
        return valueHolder?.value?.value ?: fallbackValueProvider()
    }

    override fun get(): Next<T, E> = getValueHolder().getOrThrow()

    override fun getOrNull(): Next<T, E>? = getValueHolder().getOrNull()

    override fun getOr(fallbackValue: Next<T, E>) = getValueHolder().getOr(fallbackValue)

    override fun getOr(fallbackValueProvider: () -> Next<T, E>) = getValueHolder().getOr(fallbackValueProvider)

    override fun isSet(): Boolean = getValueHolder().isSet()

    override fun doIfSet(action: (Next<T, E>) -> Unit) = getValueHolder().doIfSet(action)

    override fun getValueEntity(): Value<T> {
        return reflection
    }

}