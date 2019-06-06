package net.apptronic.core.component.entity.entities

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.subscriptions.ContextSubscriptionFactory

class EmptyChain(
        private val context: Context
) : Entity<Unit> {

    private val subscriptionFactory = ContextSubscriptionFactory<Unit>(context)

    override fun getContext(): Context {
        return context
    }

    override fun subscribe(context: Context, observer: Observer<Unit>): EntitySubscription {
        observer.notify(Unit)
        return subscriptionFactory.using(context).createSubscription(observer)
    }

}