package net.apptronic.core.entity.reflection

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.subject.*
import net.apptronic.core.context.Context
import net.apptronic.core.entity.BaseEntity
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.EntitySubscription
import net.apptronic.core.entity.base.MutableValue
import net.apptronic.core.entity.base.Value
import net.apptronic.core.entity.commons.asEvent
import net.apptronic.core.entity.function.map

private class LambdaMirror<T, E>(
        private val directMethod: (E) -> T,
        private val reverseMethod: (T) -> E
) : Mirror<T, E> {

    override fun direct(value: E): T = directMethod(value)

    override fun reverse(value: T): E = reverseMethod(value)

}

/**
 * Created new [Value] which reflects source [Value] with converted value.
 * Any changes made to source automatically reflected on reflection, and any changes, made to
 * reflection, automatically reflected on source.
 */
fun <T, E> Value<E>.reflect(
        direct: (E) -> T,
        reverse: (T) -> E,
): Value<T> {
    return ReflectionValue(this, LambdaMirror(direct, reverse))
}

fun <T, E> MutableValue<E>.reflect(
        direct: (E) -> T,
        reverse: (T) -> E,
): MutableValue<T> {
    return ReflectionValue(this, LambdaMirror(direct, reverse))
}

fun <T, E> Value<E>.reflect(mirror: Mirror<T, E>): Value<T> {
    return ReflectionValue(this, mirror)
}

fun <T, E> MutableValue<E>.reflect(mirror: Mirror<T, E>): MutableValue<T> {
    return ReflectionValue(this, mirror)
}

private class ReflectionValue<T, E>(
        private val target: Value<E>,
        private val mirror: Mirror<T, E>
) : BaseEntity<T>(), MutableValue<T> {

    override val context: Context = target.context

    val E.directReflection: T
        get() = mirror.direct(this)

    val T.reverseReflection: E
        get() = mirror.reverse(this)

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
        return target.getValueHolder().map(mirror::direct)
    }

    override val changes: Entity<MutableValue.Change<T>> =
            if (target is MutableValue<E>) {
                target.changes.map { MutableValue.Change(it.value.directReflection, it.isUpdate) }
            } else {
                map { MutableValue.Change(it, true) }
            }.asEvent()

    override val updates: Entity<T> =
            if (target is MutableValue<E>) {
                target.updates.map(mirror::direct)
            } else {
                target.map(mirror::direct)
            }.asEvent()

    override fun applyChange(change: MutableValue.Change<T>) {
        if (change.isUpdate) {
            target.update(mirror.reverse(change.value))
        } else {
            target.set(mirror.reverse(change.value))
        }
    }

    override fun applyChange(value: T, isUpdate: Boolean) {
        if (isUpdate) {
            target.update(mirror.reverse(value))
        } else {
            target.set(mirror.reverse(value))
        }
    }

    override fun updateValue(updateCall: (T) -> T) {
        val current = mirror.direct(target.get())
        val next = updateCall(current)
        target.update(mirror.reverse(next))
    }

    override fun get() = getValueHolder().getOrThrow()

    override fun getOrNull() = getValueHolder().getOrNull()

    override fun getOr(fallbackValue: T) = getValueHolder().getOr(fallbackValue)

    override fun getOr(fallbackValueProvider: () -> T) = getValueHolder().getOr(fallbackValueProvider)

    override fun isSet() = getValueHolder().isSet()

    override fun doIfSet(action: (T) -> Unit) = getValueHolder().doIfSet(action)

}