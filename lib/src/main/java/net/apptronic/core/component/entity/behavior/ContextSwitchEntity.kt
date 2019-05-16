package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.subscribe

class ContextSwitchEntity<T>(
    private val target: Entity<T>,
    private val context: Context
) : Entity<T> {

    override fun getContext(): Context {
        return context
    }

    override fun subscribe(observer: Observer<T>): EntitySubscription {
        return target.subscribe(context) { value ->
            observer.notify(value)
        }
    }

}