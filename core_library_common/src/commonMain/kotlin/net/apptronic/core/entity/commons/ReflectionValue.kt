package net.apptronic.core.entity.commons

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.subject.ValueHolder
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
        return target.getValueHolder()?.let { ValueHolder(it.value.directReflection) }
    }

    override val notifications: Entity<MutableValue.Notification<T>> =
            if (target is MutableValue<E>) {
                target.notifications.map { MutableValue.Notification(it.value.directReflection, it.isUpdate) }
            } else {
                map { MutableValue.Notification(it, true) }
            }

}