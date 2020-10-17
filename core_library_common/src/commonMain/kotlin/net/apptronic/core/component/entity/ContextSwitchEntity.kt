package net.apptronic.core.component.entity

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context

internal class ContextSwitchEntity<T>(
        private val source: Entity<T>,
        private val targetContext: Context
) : Entity<T> {

    override val context: Context = targetContext

    override fun subscribe(observer: Observer<T>): EntitySubscription {
        return source.subscribe(targetContext, observer)
    }

    override fun subscribe(targetContext: Context, observer: Observer<T>): EntitySubscription {
        return source.subscribe(targetContext, observer)
    }

}