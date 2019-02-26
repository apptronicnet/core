package net.apptronic.common.core.component.entity.entities

import net.apptronic.common.core.component.ComponentContext
import net.apptronic.common.core.component.entity.Predicate
import net.apptronic.common.core.component.entity.base.UpdatePredicate

abstract class Property<T>(
    context: ComponentContext,
    predicate: UpdatePredicate<T>
) : ComponentEntity<T>(
    context,
    predicate
), Predicate<T> {

    fun set(value: T) {
        workingPredicate.update(value)
        onSetValue(value)
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

    /**
     * Subscribe to updates of [source] and set all new values automatically
     */
    fun setAs(source: Predicate<T>) {
        source.subscribe {
            set(it)
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

fun <E : Property<T>, T> E.setAs(predicate: Predicate<T>): E {
    predicate.subscribe {
        set(it)
    }
    return this
}