package net.apptronic.core.entity.behavior

import kotlinx.coroutines.launch
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.context.coroutines.lifecycleCoroutineScope
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.RelayEntity

/**
 * Creates new [Entity] which emits all items of source entity asynchronously.
 */
fun <T> Entity<T>.async(): Entity<T> {
    return AsyncEntity(this)
}

private class AsyncEntity<T>(source: Entity<T>) : RelayEntity<T>(source) {

    private val asyncCoroutineScope = context.lifecycleCoroutineScope

    override fun onNext(nextValue: T, observer: Observer<T>) {
        asyncCoroutineScope.launch {
            observer.update(nextValue)
        }
    }

}