package net.apptronic.core.lang.flow

import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.apptronic.core.base.observable.BasicSubscription
import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.Subscription
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.coroutines.createLifecycleCoroutineScope
import net.apptronic.core.component.coroutines.mainDispatcher
import net.apptronic.core.component.entity.BaseEntity
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.entities.performEntitySubscription

/**
 * Takes [Flow] and creates new [Entity] from all values emitted by this flow for each new [Observer].
 */
fun <T> Flow<T>.asEntity(context: Context): Entity<T> {
    return FlowEntity(context, this)
}

class FlowEntity<T> internal constructor(
        override val context: Context,
        private val flow: Flow<T>
) : BaseEntity<T>() {

    override fun onSubscribeObserver(targetContext: Context, targetObserver: Observer<T>): EntitySubscription {
        return performEntitySubscription(targetContext, FlowObservable(), targetObserver)
    }

    private inner class FlowObservable : Observable<T> {

        private val mainDispatcher = context.mainDispatcher

        override fun subscribe(observer: Observer<T>): Subscription {
            val scope = context.createLifecycleCoroutineScope()
            val subscription = BasicSubscription(observer)
            val job = scope.launch {
                flow.collect { next ->
                    withContext(mainDispatcher) {
                        subscription.notify(next)
                    }
                }
            }
            subscription.doOnUnsubscribe {
                scope.cancel()
                job.cancel()
            }
            return subscription
        }

    }

}