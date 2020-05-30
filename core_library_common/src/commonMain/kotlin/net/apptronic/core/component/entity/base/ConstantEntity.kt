package net.apptronic.core.component.entity.base

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.base.observable.subject.Subject
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.subscriptions.ContextSubscriptionFactory

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