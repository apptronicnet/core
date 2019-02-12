package net.apptronic.common.core.component.entity.entities

import net.apptronic.common.core.component.ComponentContext
import net.apptronic.common.core.component.entity.Predicate
import net.apptronic.common.core.component.entity.base.UpdatePredicate

abstract class LiveModelProperty<T>(
    context: ComponentContext,
    predicate: UpdatePredicate<T>
) : LiveModelEntity<T>(
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

    fun setFrom(property: LiveModelProperty<T>): Boolean {
        return try {
            set(property.get())
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

fun <E : LiveModelProperty<T>, T> E.setAs(predicate: Predicate<T>): E {
    predicate.subscribe {
        set(it)
    }
    return this
}