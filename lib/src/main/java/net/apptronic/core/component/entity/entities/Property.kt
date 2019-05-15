package net.apptronic.core.component.entity.entities

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.base.UpdatePredicate
import net.apptronic.core.component.entity.subscribe

abstract class Property<T>(
    internal val context: Context,
    predicate: UpdatePredicate<T>
) : ComponentEntity<T>(
    context,
    predicate
) {

    fun set(value: T) {
        onSetValue(value)
        workingPredicate.update(value)
    }

    abstract fun isSet(): Boolean

    protected abstract fun onSetValue(value: T)

    protected abstract fun onGetValue(): T

    /**
     * Set current value from given [source]
     * @return true if value set, false if no value set inside [source]
     */
    fun setFrom(source: Property<T>): Boolean {
        return try {
            set(source.get())
            true
        } catch (e: PropertyNotSetException) {
            false
        }
    }

    fun get(): T {
        return onGetValue()
    }

    fun getOrNull(): T? {
        return try {
            get()
        } catch (e: PropertyNotSetException) {
            null
        }
    }

    fun doIfSet(action: (T) -> Unit): Boolean {
        return try {
            action(get())
            true
        } catch (e: PropertyNotSetException) {
            false
        }
    }

}

/**
 * Subscribe to updates of [source] and set all new values automatically
 */
fun <E : Property<T>, T> E.setAs(predicate: Predicate<T>): E {
    val subscription = predicate.subscribe(context) {
        set(it)
    }
    this.context.getLifecycle().onExitFromActiveStage {
        subscription.unsubscribe()
    }
    return this
}

/**
 * Subscribe to updates of [source] and set all new values automatically
 */
fun <T> Predicate<T>.setTo(entity: Property<T>): Predicate<T> {
    entity.setAs(this)
    return this
}

fun <T> Predicate<T>.mirrorValue(context: Context): ComponentEntity<T> {
    val result = MutableValue<T>(context)
    result.setAs(this)
    return result
}

fun <T> Predicate<T>.mirrorTypedEvent(context: Context): ComponentEntity<T> {
    val result = ComponentTypedEvent<T>(context)
    subscribe {
        result.send(it)
    }
    return result
}

fun Predicate<*>.mirrorGenericEvent(context: Context): ComponentEntity<Unit> {
    val result = ComponentTypedEvent<Unit>(context)
    subscribe {
        result.send(Unit)
    }
    return result
}