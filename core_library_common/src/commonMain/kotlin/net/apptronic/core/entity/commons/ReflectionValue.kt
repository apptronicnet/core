package net.apptronic.core.entity.commons

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.subject.*
import net.apptronic.core.context.Context
import net.apptronic.core.entity.BaseEntity
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.EntitySubscription
import net.apptronic.core.entity.base.MutableValue
import net.apptronic.core.entity.base.Value
import net.apptronic.core.entity.function.map

/**
 * Created new [MutableValue] which reflects source [MutableValue] with converted value.
 * Any changes made to source automatically reflected on reflection, and any changes, made to
 * reflection, automatically reflected on source.
 */
fun <T, E> Value<E>.reflect(
        direct: (E) -> T,
        reverse: (T) -> E,
): Value<T> {
    return ReflectionValue(this, direct, reverse)
}

fun <T, E> MutableValue<E>.reflect(
        direct: (E) -> T,
        reverse: (T) -> E,
): MutableValue<T> {
    return ReflectionValue(this, direct, reverse)
}

private class ReflectionValue<T, E>(
        private val target: Value<E>,
        private val directReflection: (E) -> T,
        private val reverseReflection: (T) -> E,
) : BaseEntity<T>(), MutableValue<T> {

    override val context: Context = target.context

    val E.directReflection: T
        get() = directReflection(this)

    val T.reverseReflection: E
        get() = reverseReflection(this)

    override fun onSubscribeObserver(targetContext: Context, targetObserver: Observer<T>): EntitySubscription {
        return target.subscribe(context) {
            targetObserver.update(it.directReflection)
        }
    }

    override fun set(value: T) {
        target.set(value.reverseReflection)
    }

    override fun update(value: T) {
        target.update(value.reverseReflection)
    }

    override fun getValueHolder(): ValueHolder<T>? {
        return target.getValueHolder().map(directReflection)
    }

    override val changes: Entity<MutableValue.Change<T>> =
            if (target is MutableValue<E>) {
                target.changes.map { MutableValue.Change(it.value.directReflection, it.isUpdate) }
            } else {
                map { MutableValue.Change(it, true) }
            }.asEvent()

    override val updates: Entity<T> =
            if (target is MutableValue<E>) {
                target.updates.map(directReflection)
            } else {
                target.map(directReflection)
            }.asEvent()

    override fun applyChange(change: MutableValue.Change<T>) {
        if (change.isUpdate) {
            target.update(reverseReflection(change.value))
        } else {
            target.set(reverseReflection(change.value))
        }
    }

    override fun applyChange(value: T, isUpdate: Boolean) {
        if (isUpdate) {
            target.update(reverseReflection(value))
        } else {
            target.set(reverseReflection(value))
        }
    }

    override fun updateValue(updateCall: (T) -> T) {
        val current = directReflection(target.get())
        val next = updateCall(current)
        target.update(reverseReflection(next))
    }

    override fun get() = getValueHolder().getOrThrow()

    override fun getOrNull() = getValueHolder().getOrNull()

    override fun getOr(fallbackValue: T) = getValueHolder().getOr(fallbackValue)

    override fun getOr(fallbackValueProvider: () -> T) = getValueHolder().getOr(fallbackValueProvider)

    override fun isSet() = getValueHolder().isSet()

    override fun doIfSet(action: (T) -> Unit) = getValueHolder().doIfSet(action)

}