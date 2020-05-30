package net.apptronic.core.component.entity.entities

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.*
import net.apptronic.core.component.entity.base.EntityValue
import net.apptronic.core.component.entity.base.ObservableEntity
import net.apptronic.core.component.entity.base.UpdateEntity

abstract class Property<T>(override val context: Context) : ObservableEntity<T>(), EntityValue<T> {

    protected val subject = BehaviorSubject<T>()

    override val observable: Observable<T> = subject

    override fun getValueHolder(): ValueHolder<T>? {
        return subject.getValue()
    }

    override fun toString(): String {
        val valueHolder = subject.getValue()
        return super.toString() + if (valueHolder == null) "/not-set" else "=$valueHolder"
    }

}

/**
 * Subscribe to updates of [source] and set all new values automatically
 */
fun <E : UpdateEntity<T>, T> E.setAs(source: Entity<T>): E {
    source.subscribe(context) {
        update(it)
    }
    return this
}

/**
 * Subscribe to updates of [source] and set all new values automatically
 */
fun <T> Entity<T>.setTo(target: UpdateEntity<T>): Entity<T> {
    target.setAs(this)
    return this
}

fun <T> Entity<T>.mirrorProperty(context: Context): Property<T> {
    val result = MutableValue<T>(context)
    result.setAs(this)
    return result
}

fun <T> Entity<T>.mirrorEvent(context: Context): Event<T> {
    val result = TypedEvent<T>(context)
    subscribe {
        result.send(it)
    }
    return result
}

fun Entity<*>.mirrorGenericEvent(context: Context): Event<Unit> {
    val result = TypedEvent<Unit>(context)
    subscribe {
        result.send(Unit)
    }
    return result
}