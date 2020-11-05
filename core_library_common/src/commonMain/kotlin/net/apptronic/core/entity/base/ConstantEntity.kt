package net.apptronic.core.entity.base

import net.apptronic.core.base.subject.BehaviorSubject
import net.apptronic.core.base.subject.ValueHolder
import net.apptronic.core.context.Context

/**
 * Entity with constant value
 */
class ConstantEntity<T>(
        override val context: Context,
        private val value: T
) : SubjectEntity<T>(), EntityValue<T> {

    override val subject = BehaviorSubject<T>()

    init {
        subject.update(value)
    }

    override fun getValueHolder(): ValueHolder<T>? {
        return subject.getValue()
    }

}