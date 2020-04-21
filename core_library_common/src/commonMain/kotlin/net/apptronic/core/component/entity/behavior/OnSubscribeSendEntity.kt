package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription

fun <T> Entity<T>.onSubscribe(onSubscribeValueProvider: () -> T): Entity<T> {
    return OnSubscribeSendEntity(this, onSubscribeValueProvider)
}

fun <T> Entity<T>.onSubscribe(onSubscribeValue: T): Entity<T> {
    return OnSubscribeSendEntity(this) { onSubscribeValue }
}

private class OnSubscribeSendEntity<T>(
        private val source: Entity<T>,
        private val onSubscribeValueProvider: () -> T
) : Entity<T> {

    override val context = source.context

    override fun subscribe(context: Context, observer: Observer<T>): EntitySubscription {
        observer.notify(onSubscribeValueProvider.invoke())
        return source.asEvent().subscribe(context, observer)
    }

}