package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.elapsedRealtimeMillis
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.coroutines.CoroutineLauncher
import net.apptronic.core.component.coroutines.coroutineLauncherScoped
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.base.RelayEntity

// TODO missing documentation
fun <T> Entity<T>.debounce(
        interval: Long,
        delay: Long = 0
): Entity<T> {
    return DebounceEntity(this, interval, delay)
}

fun <T> Entity<T>.debounceAndStore(
        interval: Long,
        delay: Long = 0
): Entity<T> {
    return debounce(interval, delay).storeLatest()
}

private class DebounceEntity<T>(
        source: Entity<T>,
        val interval: Long,
        val delay: Long
) : RelayEntity<T>(source) {

    override fun proceedObserver(targetContext: Context, target: Observer<T>): Observer<T> {
        val coroutineLauncher = targetContext.coroutineLauncherScoped()
        return DebounceObserver(coroutineLauncher, target, interval, delay)
    }

}

private class DebounceObserver<T>(
        private val coroutineLauncher: CoroutineLauncher,
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
                coroutineLauncher.launch {
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
                coroutineLauncher.launch {
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