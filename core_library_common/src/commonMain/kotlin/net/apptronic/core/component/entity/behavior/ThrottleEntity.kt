package net.apptronic.core.component.entity.behavior

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.apptronic.core.base.elapsedRealtimeMillis
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.coroutines.lifecycleCoroutineScope
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.base.EntityValue
import net.apptronic.core.component.entity.base.RelayEntity
import net.apptronic.core.component.entity.entities.asProperty

/**
 * Creates new [Entity] which re-emits items from source, but applying filtering to prevent emitting items
 * too frequently.
 *
 * @param interval min interval for emitting items. If next item from source is emitted before [interval] ended
 * it will be stored. When [interval] passed only last stored value will be emitted.
 * @param delay adds min delay for emitting next item from source.
 */
fun <T> Entity<T>.throttle(
        interval: Long,
        delay: Long = 0
): Entity<T> {
    return ThrottleEntity(this, interval, delay)
}

fun <T> Entity<T>.throttleAndStore(
        interval: Long,
        delay: Long = 0
): EntityValue<T> {
    return throttle(interval, delay).asProperty()
}

private class ThrottleEntity<T>(
        source: Entity<T>,
        val interval: Long,
        val delay: Long
) : RelayEntity<T>(source) {

    override fun onNewObserver(targetContext: Context, observer: Observer<T>): Observer<T> {
        val coroutineScope = targetContext.lifecycleCoroutineScope
        return ThrottleObserver(coroutineScope, observer, interval, delay)
    }

}

private class ThrottleObserver<T>(
        private val coroutineScope: CoroutineScope,
        val target: Observer<T>,
        val interval: Long,
        val delay: Long
) : Observer<T> {

    private class Update<T>(val time: Long, val value: T)

    private var update: Update<T>? = null
    private var isWaiting = false

    override fun notify(value: T) {
        onNext(value)
    }

    private fun onNext(value: T) {
        val lastUpdate = update
        if (lastUpdate == null || elapsedRealtimeMillis() - lastUpdate.time >= interval) {
            update = Update(elapsedRealtimeMillis(), value)
            if (delay > 0) {
                coroutineScope.launch {
                    if (delay > 0) {
                        kotlinx.coroutines.delay(delay)
                    }
                    sendNext()
                }
            } else {
                target.notify(value)
            }
        } else {
            update = Update(lastUpdate.time, value)
            if (!isWaiting) {
                isWaiting = true
                val timeFromLast = elapsedRealtimeMillis() - lastUpdate.time
                val timeToNext = interval - timeFromLast
                val waitTime = if (timeToNext < delay) delay else timeToNext
                coroutineScope.launch {
                    if (waitTime > 0) {
                        kotlinx.coroutines.delay(waitTime)
                    }
                    sendNext()
                }
            }
        }
    }

    private fun sendNext() {
        isWaiting = true
        val next = update
        if (next != null) {
            target.notify(next.value)
        }
    }

}