package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.concurrent.Synchronized
import net.apptronic.core.base.concurrent.Volatile
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.platform.elapsedRealtimeMillis
import net.apptronic.core.platform.pauseCurrentThread
import net.apptronic.core.threading.Worker
import net.apptronic.core.threading.WorkerDefinition
import net.apptronic.core.threading.execute

fun <T> Entity<T>.debounce(
        interval: Long,
        delay: Long = 0,
        targetWorker: WorkerDefinition = WorkerDefinition.DEFAULT
): Entity<T> {
    return DebounceEntity(this, interval, delay, targetWorker)
}

fun <T> Entity<T>.debounceAndStore(
        interval: Long,
        delay: Long = 0,
        targetWorker: WorkerDefinition = WorkerDefinition.DEFAULT
): Entity<T> {
    return debounce(interval, delay, targetWorker).storeLatest()
}

private class DebounceEntity<T>(
        val target: Entity<T>,
        val interval: Long,
        val delay: Long,
        val targetWorker: WorkerDefinition
) : Entity<T> {

    override val context: Context = target.context

    override fun subscribe(context: Context, observer: Observer<T>): EntitySubscription {
        val delayWorker = context.getScheduler().getWorker(WorkerDefinition.TIMER)
        val resultWorker = context.getScheduler().getWorker(targetWorker)
        val targetObserver = DebounceObserver(delayWorker, resultWorker, observer, interval, delay)
        return target.subscribe(context, targetObserver)
    }

}

private class DebounceObserver<T>(
        val timerWorker: Worker,
        val resultWorker: Worker,
        val target: Observer<T>,
        val interval: Long,
        val delay: Long
) : Observer<T> {

    class Update<T>(val time: Long, val value: T)

    val sync = Synchronized()
    val update = Volatile<Update<T>?>(null)
    val isWaiting = Volatile(false)

    override fun notify(value: T) {
        sync.executeBlock {
            onNext(value)
        }
    }

    private fun onNext(value: T) {
        val lastUpdate = update.get()
        if (lastUpdate == null || elapsedRealtimeMillis() - lastUpdate.time >= interval) {
            update.set(Update(elapsedRealtimeMillis(), value))
            if (delay > 0) {
                timerWorker.execute {
                    if (delay > 0) {
                        pauseCurrentThread(delay)
                    }
                    sendNext()
                }
            } else {
                resultWorker.execute {
                    target.notify(value)
                }
            }
        } else {
            update.set(Update(lastUpdate.time, value))
            if (isWaiting.get().not()) {
                isWaiting.set(true)
                val timeFromLast = elapsedRealtimeMillis() - lastUpdate.time
                val timeToNext = interval - timeFromLast
                val waitTime = if (timeToNext < delay) delay else timeToNext
                timerWorker.execute {
                    if (waitTime > 0) {
                        pauseCurrentThread(waitTime)
                    }
                    sendNext()
                }
            }
        }
    }

    private fun sendNext() {
        sync.executeBlock {
            isWaiting.set(true)
            val next = update.get()
            if (next != null) {
                resultWorker.execute {
                    target.notify(next.value)
                }
            }
        }
    }

}