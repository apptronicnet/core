package net.apptronic.core.entity.behavior

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.context.Context
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.RelayEntity
import net.apptronic.core.entity.commons.asEvent

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

    override fun onNewObserver(targetContext: Context, observer: Observer<T>): Observer<T> {
        observer.notify(onSubscribeValueProvider.invoke())
        return super.onNewObserver(targetContext, observer)
    }

}