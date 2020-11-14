package net.apptronic.core.entity.behavior

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.subject.ValueHolder
import net.apptronic.core.context.Context
import net.apptronic.core.context.coroutines.lifecycleCoroutineScope
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.RelayEntity

/**
 * Consumes source entity asynchronously taking only last emitted value on each change synchronous. If source value
 * changing synchronously many times it will emit only last value.
 */
fun <T> Entity<T>.throttleAsync(): Entity<T> {
    return ThrottleAsyncEntity(this)
}

private class ThrottleAsyncEntity<T>(
        source: Entity<T>
) : RelayEntity<T>(source) {

    override fun onNewObserver(targetContext: Context, observer: Observer<T>): Observer<T> {
        return ThrottleAsyncObserver(targetContext.lifecycleCoroutineScope, observer)
    }

    private class ThrottleAsyncObserver<T>(
            private val coroutineScope: CoroutineScope,
            private val target: Observer<T>
    ) : Observer<T> {

        private var nextValue: ValueHolder<T>? = null
        private var isProcessing = false

        override fun update(value: T) {
            nextValue = ValueHolder(value)
            if (!isProcessing) {
                isProcessing = true
                coroutineScope.launch {
                    isProcessing = false
                    nextValue?.let { valueHolder ->
                        target.update(valueHolder.value)
                    }
                    nextValue = null
                }
            }
        }

    }

}