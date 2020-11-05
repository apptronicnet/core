package net.apptronic.core.entity.behavior

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.context.Context
import net.apptronic.core.context.coroutines.lifecycleCoroutineScope
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.RelayEntity

fun <T> Entity<T>.delay(timeInMillis: Long): Entity<T> {
    return DelayEntity(this) { timeInMillis }
}

fun <T> Entity<T>.delay(timeInMillisProvider: (T) -> Long): Entity<T> {
    return DelayEntity(this, timeInMillisProvider)
}

private class DelayEntity<T>(
         source: Entity<T>,
        private val timeInMillisProvider: (T) -> Long
) : RelayEntity<T>(source) {

    override fun onNewObserver(targetContext: Context, observer: Observer<T>): Observer<T> {
        val coroutineScope = targetContext.lifecycleCoroutineScope
        return DelayObserver(observer, coroutineScope, timeInMillisProvider)
    }

    private class DelayObserver<T>(
            private val target: Observer<T>,
            private val coroutineScope: CoroutineScope,
            private val timeInMillisProvider: (T) -> Long
    ) : Observer<T> {

        override fun notify(value: T) {
            coroutineScope.launch {
                val timeInMillis = timeInMillisProvider.invoke(value)
                if (timeInMillis > 0L) {
                    kotlinx.coroutines.delay(timeInMillis)
                }
                target.notify(value)
            }
        }

    }

}