package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.base.RelayEntity
import net.apptronic.core.component.entity.entities.asEvent

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