package net.apptronic.core.component.entity.entities

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription

class EmptyChain(
    private val context: Context
) : Entity<Unit> {

    override fun getContext(): Context {
        return context
    }

    override fun subscribe(observer: Observer<Unit>): EntitySubscription {
        observer.notify(Unit)
        return StubSubscription()
    }

    private class StubSubscription : EntitySubscription {
        override fun unsubscribe() {
            // do nothing
        }
    }

}