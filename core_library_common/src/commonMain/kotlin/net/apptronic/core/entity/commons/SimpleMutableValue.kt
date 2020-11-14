package net.apptronic.core.entity.commons

import net.apptronic.core.base.subject.PublishSubject
import net.apptronic.core.base.utils.EqComparator
import net.apptronic.core.base.utils.SimpleEqComparator
import net.apptronic.core.context.Context
import net.apptronic.core.entity.base.MutableValue
import net.apptronic.core.entity.bindContext

class SimpleMutableValue<T> internal constructor(
        context: Context,
        private val eqComparator: EqComparator<T> = SimpleEqComparator()
) : SimpleProperty<T>(context), MutableValue<T> {

    private val notificationsSubject = PublishSubject<MutableValue.Notification<T>>()

    override val notifications = notificationsSubject.bindContext(context)

    private fun setInternal(value: T, isUpdate: Boolean) {
        val holder = getValueHolder()
        if (holder != null) {
            if (!eqComparator.isEqualsNullable(holder.value, value)) {
                subject.update(value)
                notificationsSubject.update(MutableValue.Notification(value, isUpdate))
            }
        } else {
            subject.update(value)
            notificationsSubject.update(MutableValue.Notification(value, isUpdate))
        }
    }

    override fun set(value: T) {
        setInternal(value, false)
    }

    override fun update(value: T) {
        setInternal(value, true)
    }

    fun updateValue(updateCall: (T) -> T) {
        val current = get()
        val next = updateCall(current)
        update(next)
    }

}

