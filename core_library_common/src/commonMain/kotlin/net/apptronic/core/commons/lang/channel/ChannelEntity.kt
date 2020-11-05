package net.apptronic.core.commons.lang.channel

import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import net.apptronic.core.UnderDevelopment
import net.apptronic.core.base.observable.BasicSubscription
import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.Subscription
import net.apptronic.core.context.Context
import net.apptronic.core.context.coroutines.createLifecycleCoroutineScope
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.EntitySubscription
import net.apptronic.core.entity.entities.performEntitySubscription

@UnderDevelopment
fun <T> ReceiveChannel<T>.asEntity(context: Context): Entity<T> {
    return ChannelEntity(context, this)
}

@UnderDevelopment
class ChannelEntity<T>(override val context: Context, private val channel: ReceiveChannel<T>) : Entity<T> {

    override fun subscribe(targetContext: Context, observer: Observer<T>): EntitySubscription {
        return performEntitySubscription(targetContext, ChannelObservable(), observer)
    }

    private inner class ChannelObservable : Observable<T> {

        override fun subscribe(observer: Observer<T>): Subscription {
            val scope = context.createLifecycleCoroutineScope()
            val subscription = BasicSubscription(observer)
            subscription.doOnUnsubscribe {
                scope.cancel()
            }
            scope.launch {
                try {
                    while (true) {
                        val next = channel.receive()
                        subscription.notify(next)
                    }
                } catch (e: ClosedReceiveChannelException) {
                    // ignore
                }
            }
            return subscription
        }

    }

}