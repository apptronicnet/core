package net.apptronic.core.commons.lang.flow

import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import net.apptronic.core.UnderDevelopment
import net.apptronic.core.base.collections.LinkedQueue
import net.apptronic.core.base.observable.BasicSubscription
import net.apptronic.core.context.coroutines.createLifecycleCoroutineScope
import net.apptronic.core.context.coroutines.mainDispatcher
import net.apptronic.core.entity.Entity

/**
 * This method creates [Flow] from [Entity].
 */
@UnderDevelopment
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