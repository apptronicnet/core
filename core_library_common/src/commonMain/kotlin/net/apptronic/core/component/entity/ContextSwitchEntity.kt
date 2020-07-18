package net.apptronic.core.component.entity

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context

fun <T> Entity<T>.switchContext(
        targetContext: Context
): Entity<T> {
    return if (this.context != targetContext) {
        ContextSwitchEntity(this, targetContext)
    } else {
        this
    }
}

private class ContextSwitchEntity<T>(
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