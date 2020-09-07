package net.apptronic.core.lang.flow

import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import net.apptronic.core.base.collections.LinkedQueue
import net.apptronic.core.base.observable.BasicSubscription
import net.apptronic.core.component.coroutines.createLifecycleCoroutineScope
import net.apptronic.core.component.coroutines.mainDispatcher
import net.apptronic.core.component.entity.Entity

/**
 * This method creates [Flow] from [Entity].
 */
fun <T> Entity<T>.asFlow(): Flow<T> {
    val queue = LinkedQueue<T>()
    val subscription = BasicSubscription(queue)
    val scope = context.createLifecycleCoroutineScope()
    subscription.doOnUnsubscribe {
        scope.cancel()
    }
    subscribe(subscription)
    return flow {
        scope.launch(context.mainDispatcher) {
            while (true) {
                emit(queue.takeAwait())
            }
        }
    }
}