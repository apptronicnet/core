package net.apptronic.core.component.entity.entities

import net.apptronic.core.base.concurrent.requireNeverFrozen
import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.subscriptions.ContextSubscriptionFactory

abstract class ComponentEntity<T>(
        override val context: Context
) : Entity<T> {

    init {
        requireNeverFrozen()
    }

    private val subscriptionFactory = ContextSubscriptionFactory<T>(context)

    protected abstract fun getObservable(): Observable<T>

    override fun subscribe(context: Context, observer: Observer<T>): EntitySubscription {
        return subscriptionFactory.using(context).subscribe(observer, getObservable())
    }

}