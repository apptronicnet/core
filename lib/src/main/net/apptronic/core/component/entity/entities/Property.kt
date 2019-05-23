package net.apptronic.core.component.entity.entities

import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.*

abstract class Property<T>(context: Context) : ComponentEntity<T>(context), EntityValue<T>,
    UpdateEntity<T> {

    protected val subject = BehaviorSubject<T>()

    override fun getValueHolder(): ValueHolder<T>? {
        return subject.getValue()
    }

    fun set(value: T) {
        subject.update(value)
    }

    override fun update(value: T) {
        set(value)
    }

    override fun toString(): String {
        val valueHolder = subject.getValue()
        return super.toString() + if (valueHolder == null) "/not-set" else "=$valueHolder"
    }

    /**
     * Set current value from given [source]
     * @return true if value set, false if no value set inside [source]
     */
    fun setFrom(source: Property<T>): Boolean {
        return try {
            set(source.get())
            true
        } catch (e: ValueNotSetException) {
            false
        }
    }

}

/**
 * Subscribe to updates of [source] and set all new values automatically
 */
fun <E : UpdateEntity<T>, T> E.setAs(source: Entity<T>): E {
    source.subscribe(getContext()) {
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