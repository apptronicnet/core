package net.apptronic.core.component.entity.base

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.EntityValue
import net.apptronic.core.component.entity.subscriptions.ContextSubscriptionFactory

/**
 * Entity with constant value
 */
class ConstantEntity<T>(
        private val context: Context,
        private val value: T
) : EntityValue<T> {

    private val subscriptionFactory = ContextSubscriptionFactory<T>(context)

    private val valueHolder = ValueHolder(value)

    override fun getContext(): Context {
        return context
    }

    override fun getValueHolder(): ValueHolder<T>? {
        return valueHolder
    }

    override fun subscribe(context: Context, observer: Observer<T>): EntitySubscription {
        return subscriptionFactory.using(context).createSubscription(observer).also {
            it.notify(value)
        }
    }

}