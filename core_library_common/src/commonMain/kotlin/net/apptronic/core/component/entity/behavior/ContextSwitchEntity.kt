package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.subscribe

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