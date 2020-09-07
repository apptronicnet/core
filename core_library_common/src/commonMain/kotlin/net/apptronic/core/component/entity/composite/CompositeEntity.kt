package net.apptronic.core.component.entity.composite

import net.apptronic.core.base.collections.queueOf
import net.apptronic.core.base.observable.BasicSubscription
import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.Subscription
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.*
import net.apptronic.core.component.entity.entities.performEntitySubscription

class CompositeEntity<E, T>(
        sources: List<Entity<out E>>,
        private val composeHandler: ComposeHandler<E, T>
) : BaseEntity<T>() {

    private val sources = sources.toTypedArray()

    override val context: Context = collectContext(*sources.toTypedArray())

    override fun onSubscribeObserver(targetContext: Context, targetObserver: Observer<T>): EntitySubscription {
        return performEntitySubscription(targetContext, CompositeObservable(), targetObserver)
    }

    private inner class CompositeObservable : Observable<T> {

        override fun subscribe(observer: Observer<T>): Subscription {
            val subscription = BasicSubscription(observer)
            val queue = queueOf<ComposedNext<E>>()
            sources.forEachIndexed { index, entity ->
                entity.subscribe { next ->
                    queue.add(ComposedNext(next, index))
                }.let {
                    subscription.doOnUnsubscribe {
                        it.unsubscribe()
                    }
                }
            }
            composeHandler.compose(queue, sources.size, subscription)
            return subscription
        }

    }

}