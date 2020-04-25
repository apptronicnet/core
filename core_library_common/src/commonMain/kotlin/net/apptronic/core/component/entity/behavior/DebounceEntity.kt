package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.coroutines.CoroutineLauncher
import net.apptronic.core.component.coroutines.coroutineLauncherScoped
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.platform.elapsedRealtimeMillis

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
        val target: Entity<T>,
        val interval: Long,
        val delay: Long
) : Entity<T> {

    override val context: Context = target.context

    override fun subscribe(context: Context, observer: Observer<T>): EntitySubscription {
        val coroutineLauncher = context.coroutineLauncherScoped()
        val targetObserver = DebounceObserver(coroutineLauncher, observer, interval, delay)
        return target.subscribe(context, targetObserver)
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