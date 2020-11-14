package net.apptronic.core.entity.commons

import net.apptronic.core.base.subject.PublishSubject
import net.apptronic.core.base.utils.EqComparator
import net.apptronic.core.base.utils.SimpleEqComparator
import net.apptronic.core.context.Context
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.MutableValue
import net.apptronic.core.entity.behavior.filter
import net.apptronic.core.entity.bindContext
import net.apptronic.core.entity.function.map

/**
 * Base reference implementation of [MutableValue]
 */
open class BaseMutableValue<T> internal constructor(
        context: Context,
        private val eqComparator: EqComparator<T> = SimpleEqComparator()
) : BaseProperty<T>(context), MutableValue<T> {

    private val notificationsSubject = PublishSubject<MutableValue.Change<T>>()

    final override val changes = notificationsSubject.bindContext(context)

    private fun setInternal(value: T, isUpdate: Boolean) {
        val holder = getValueHolder()
        if (holder != null) {
            if (!eqComparator.isEqualsNullable(holder.value, value)) {
                subject.update(value)
                notificationsSubject.update(MutableValue.Change(value, isUpdate))
            }
        } else {
            subject.update(value)
            notificationsSubject.update(MutableValue.Change(value, isUpdate))
        }
    }

    final override fun set(value: T) {
        setInternal(value, false)
    }

    final override fun update(value: T) {
        setInternal(value, true)
    }

    final override fun updateValue(updateCall: (T) -> T) {
        val current = get()
        val next = updateCall(current)
        setInternal(next, true)
    }

    final override val updates: Entity<T> = changes.filter { it.isUpdate }.map { it.value }

    final override fun applyChange(change: MutableValue.Change<T>) {
        if (change.isUpdate) {
            update(change.value)
        } else {
            set(change.value)
        }
    }

    final override fun applyChange(value: T, isUpdate: Boolean) {
        if (isUpdate) {
            update(value)
        } else {
            set(value)
        }
    }

}

