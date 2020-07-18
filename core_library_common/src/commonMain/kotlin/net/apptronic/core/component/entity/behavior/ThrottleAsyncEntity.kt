package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.coroutines.CoroutineLauncher
import net.apptronic.core.component.coroutines.coroutineLauncherScoped
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.base.RelayEntity

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
        return ThrottleAsyncObserver(targetContext.coroutineLauncherScoped(), observer)
    }

    private class ThrottleAsyncObserver<T>(
            private val coroutineLauncher: CoroutineLauncher,
            private val target: Observer<T>
    ) : Observer<T> {

        private var nextValue: ValueHolder<T>? = null
        private var isProcessing = false

        override fun notify(value: T) {
            nextValue = ValueHolder(value)
            if (!isProcessing) {
                isProcessing = true
                coroutineLauncher.launch {
                    isProcessing = false
                    nextValue?.let { valueHolder ->
                        target.notify(valueHolder.value)
                    }
                    nextValue = null
                }
            }
        }

    }

}