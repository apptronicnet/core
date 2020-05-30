package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.asEvent
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.base.RelayEntity

fun <T> Entity<T>.onSubscribe(onSubscribeValueProvider: () -> T): Entity<T> {
    return OnSubscribeSendEntity(this, onSubscribeValueProvider)
}

fun <T> Entity<T>.onSubscribe(onSubscribeValue: T): Entity<T> {
    return OnSubscribeSendEntity(this) { onSubscribeValue }
}

private class OnSubscribeSendEntity<T>(
        source: Entity<T>,
        private val onSubscribeValueProvider: () -> T
) : RelayEntity<T>(source.asEvent()) {

    override fun proceedObserver(targetContext: Context, target: Observer<T>): Observer<T> {
        target.notify(onSubscribeValueProvider.invoke())
        return super.proceedObserver(targetContext, target)
    }

}