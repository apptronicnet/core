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
        private val directMethod: (T) -> E,
        private val reverseMethod: (E) -> T
) : Mirror<T, E> {

    override fun direct(value: T): E = directMethod(value)

    override fun reverse(value: E): T = reverseMethod(value)

}

/**
 * Created new [Value] which reflects source [Value] with converted value.
 * Any changes made to source automatically reflected on reflection, and any changes, made to
 * reflection, automatically reflected on source.
 */
fun <T, E> Value<T>.reflect(
        direct: (T) -> E,
        reverse: (E) -> T,
): Value<E> {
    return ReflectionValue(this, LambdaMirror(direct, reverse))
}

fun <T, E> MutableValue<T>.reflectMutable(
        direct: (T) -> E,
        reverse: (E) -> T,
): MutableValue<E> {
    return ReflectionValue(this, LambdaMirror(direct, reverse))
}

fun <T, E> Value<T>.reflect(mirror: Mirror<T, E>): Value<E> {
    return ReflectionValue(this, mirror)
}

fun <T, E> MutableValue<T>.reflectMutable(mirror: Mirror<T, E>): MutableValue<E> {
    return ReflectionValue(this, mirror)
}

private class ReflectionValue<T, E>(
        private val target: Value<T>,
        private val mirror: Mirror<T, E>
) : BaseEntity<E>(), MutableValue<E> {

    override val context: Context = target.context

    val T.directReflection: E
        get() = mirror.direct(this)

    val E.reverseReflection: T
        get() = mirror.reverse(this)

    override fun onSubscribeObserver(targetContext: Context, targetObserver: Observer<E>): EntitySubscription {
        return target.subscribe(context) {
            targetObserver.update(it.directReflection)
        }
    }

    override fun set(value: E) {
        target.set(value.reverseReflection)
    }

    override fun update(value: E) {
        target.update(value.reverseReflection)
    }

    override fun getValueHolder(): ValueHolder<E>? {
        return target.getValueHolder().map(mirror::direct)
    }

    override val changes: Entity<MutableValue.Change<E>> =
            if (target is MutableValue<T>) {
                target.changes.map { MutableValue.Change(it.value.directReflection, it.isUpdate) }
            } else {
                map { MutableValue.Change(it, true) }
            }.asEvent()

    override val updates: Entity<E> =
            if (target is MutableValue<T>) {
                target.updates.map(mirror::direct)
            } else {
                target.map(mirror::direct)
            }.asEvent()

    override fun applyChange(change: MutableValue.Change<E>) {
        if (change.isUpdate) {
            target.update(mirror.reverse(change.value))
        } else {
            target.set(mirror.reverse(change.value))
        }
    }

    override fun applyChange(value: E, isUpdate: Boolean) {
        if (isUpdate) {
            target.update(mirror.reverse(value))
        } else {
            target.set(mirror.reverse(value))
        }
    }

    override fun updateValue(updateCall: (E) -> E) {
        val current = mirror.direct(target.get())
        val next = updateCall(current)
        target.update(mirror.reverse(next))
    }

    override fun get() = getValueHolder().getOrThrow()

    override fun getOrNull() = getValueHolder().getOrNull()

    override fun getOr(fallbackValue: E) = getValueHolder().getOr(fallbackValue)

    override fun getOr(fallbackValueProvider: () -> E) = getValueHolder().getOr(fallbackValueProvider)

    override fun isSet() = getValueHolder().isSet()

    override fun doIfSet(action: (E) -> Unit) = getValueHolder().doIfSet(action)

    override fun clear() {
        target.clear()
    }

}