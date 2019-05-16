package net.apptronic.core.component.entity.entities

import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.base.observable.subject.Subject
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.*

abstract class Property<T>(context: Context) : ComponentEntity<T>(context), EntityValue<T>,
    Subject<T> {

    protected val subject = BehaviorSubject<T>()

    fun set(value: T) {
        subject.update(value)
    }

    fun isSet(): Boolean {
        return subject.getValue() != null
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

    override fun get(): T {
        return subject.getValue().get()
    }

    override fun getOrNull(): T? {
        return subject.getValue().getOrNull()
    }

    fun doIfSet(action: (T) -> Unit): Boolean {
        return try {
            action(get())
            true
        } catch (e: ValueNotSetException) {
            false
        }
    }

}

/**
 * Subscribe to updates of [source] and set all new values automatically
 */
fun <E : Property<T>, T> E.setAs(source: Entity<T>): E {
    val subscription = source.subscribe(getContext()) {
        set(it)
    }
    return this
}

/**
 * Subscribe to updates of [source] and set all new values automatically
 */
fun <T> Entity<T>.setTo(source: Property<T>): Entity<T> {
    source.setAs(this)
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