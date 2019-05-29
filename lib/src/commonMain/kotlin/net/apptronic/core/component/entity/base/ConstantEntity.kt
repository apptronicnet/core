package net.apptronic.core.component.entity.base

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.EntityValue
import net.apptronic.core.component.entity.subscriptions.ContextSubscriptions

/**
 * Entity with constant value
 */
class ConstantEntity<T>(
    private val context: Context,
    private val value: T
) : EntityValue<T> {

    private val subscriptions = ContextSubscriptions<T>(context)

    private val valueHolder = ValueHolder(value)

    override fun getValueHolder(): ValueHolder<T>? {
        return valueHolder
    }

    override fun subscribe(observer: Observer<T>): EntitySubscription {
        observer.notify(value)
        return subscriptions.createSubscription(observer)
    }

    override fun getContext(): Context {
        return context
    }

}