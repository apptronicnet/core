package net.apptronic.core.component.entity.base

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.EntityValue

/**
 * Entity with constant value
 */
class ConstantEntity<T>(
    private val context: Context,
    private val value: T
) : EntityValue<T> {

    private val valueHolder = ValueHolder(value)

    override fun getValueHolder(): ValueHolder<T>? {
        return valueHolder
    }

    override fun subscribe(observer: Observer<T>): EntitySubscription {
        observer.notify(value)
        return StubSubscription()
    }

    override fun getContext(): Context {
        return context
    }

    private class StubSubscription : EntitySubscription {
        override fun unsubscribe() {
            // do nothing
        }
    }

}