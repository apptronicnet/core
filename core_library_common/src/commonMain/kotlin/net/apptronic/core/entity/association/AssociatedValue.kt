package net.apptronic.core.entity.association

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.subject.*
import net.apptronic.core.context.Context
import net.apptronic.core.entity.BaseEntity
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.EntitySubscription
import net.apptronic.core.entity.base.MutableValue
import net.apptronic.core.entity.base.Value
import net.apptronic.core.entity.commons.asEvent
import net.apptronic.core.entity.commons.distinctUntilChanged
import net.apptronic.core.entity.function.map

private class LambdaAssociation<T, E>(
    private val directMethod: (T) -> E,
    private val reverseMethod: (E) -> T
) : Association<T, E> {

    override fun direct(value: T): E = directMethod(value)

    override fun reverse(value: E): T = reverseMethod(value)

}

/**
 * Created new [Value] which associated to source [Value] with converted value.
 * Any changes made to source automatically set on associated value, and any changes, made to
 * associated value, automatically set on source.
 */
fun <T, E> Value<T>.associate(
    direct: (T) -> E,
    reverse: (E) -> T,
): Value<E> {
    return AssociatedValue(this, LambdaAssociation(direct, reverse))
}

fun <T, E> MutableValue<T>.associateMutable(
    direct: (T) -> E,
    reverse: (E) -> T,
): MutableValue<E> {
    return AssociatedValue(this, LambdaAssociation(direct, reverse))
}

fun <T, E> Value<T>.associate(association: Association<T, E>): Value<E> {
    return AssociatedValue(this, association)
}

fun <T, E> MutableValue<T>.associateMutable(association: Association<T, E>): MutableValue<E> {
    return AssociatedValue(this, association)
}

private class AssociatedValue<T, E>(
    private val target: Value<T>,
    private val association: Association<T, E>
) : BaseEntity<E>(), MutableValue<E> {

    override val context: Context = target.context

    val T.directAssociation: E
        get() = association.direct(this)

    val E.reverseAssociation: T
        get() = association.reverse(this)

    override fun onSubscribeObserver(targetContext: Context, targetObserver: Observer<E>): EntitySubscription {
        return target.map { it.directAssociation }.distinctUntilChanged().subscribe(context) {
            targetObserver.update(it)
        }
    }

    override fun set(value: E) {
        target.set(value.reverseAssociation)
    }

    override fun update(value: E) {
        target.update(value.reverseAssociation)
    }

    override fun getValueHolder(): ValueHolder<E>? {
        return target.getValueHolder().map(association::direct)
    }

    override val changes: Entity<MutableValue.Change<E>> =
            if (target is MutableValue<T>) {
                target.changes.map { MutableValue.Change(it.value.directAssociation, it.isUpdate) }
            } else {
                map { MutableValue.Change(it, true) }
            }.asEvent()

    override val updates: Entity<E> =
            if (target is MutableValue<T>) {
                target.updates.map(association::direct)
            } else {
                target.map(association::direct)
            }.asEvent()

    override fun applyChange(change: MutableValue.Change<E>) {
        if (change.isUpdate) {
            target.update(association.reverse(change.value))
        } else {
            target.set(association.reverse(change.value))
        }
    }

    override fun applyChange(value: E, isUpdate: Boolean) {
        if (isUpdate) {
            target.update(association.reverse(value))
        } else {
            target.set(association.reverse(value))
        }
    }

    override fun updateValue(updateCall: (E) -> E) {
        val current = association.direct(target.get())
        val next = updateCall(current)
        target.update(association.reverse(next))
    }

    override fun get() = getValueHolder().getOrThrow()

    override fun getOrNull() = getValueHolder().getOrNull()

    override fun getOr(fallbackValue: E) = getValueHolder().getOr(fallbackValue)

    override fun getOr(fallbackValueProvider: () -> E) = getValueHolder().getOr(fallbackValueProvider)

    override fun isSet() = getValueHolder().isSet()

    override fun isNotSet(): Boolean = !getValueHolder().isSet()

    override fun doIfSet(action: (E) -> Unit) = getValueHolder().doIfSet(action)

    override fun clear() {
        target.clear()
    }

}