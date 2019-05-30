package net.apptronic.core.component.timer

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.Component
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.platform.getPlatform
import net.apptronic.core.threading.WorkerDefinition
import net.apptronic.core.threading.execute

class Timer(
        private val component: Component,
        initialInterval: Long,
        worker: WorkerDefinition = WorkerDefinition.DEFAULT,
        initialLimit: Long = INFINITE
) {

    companion object {
        val INFINITE = -1L
    }

    private val isRunning = component.value(false)
    private val limit = component.value(initialLimit)
    private val interval = component.value(initialInterval)

    private val timerWorker = component.getScheduler().getWorker(WorkerDefinition.TIMER)
    private val timerEvent = component.typedEvent<TimerTick>().apply {
        setWorker(worker)
    }

    init {
        isRunning.subscribe {
            if (it) {
                timerWorker.execute {
                    val platform = getPlatform()
                    val interval = interval.get()
                    if (interval <= 0) {
                        isRunning.set(false)
                    }
                    val start = platform.elapsedRealtimeMillis()
                    var counter = 1L;
                    while (isRunning.get()) {
                        val total = counter * interval
                        val next = start + total
                        val current = platform.elapsedRealtimeMillis()
                        val diff = next - current
                        if (diff > 0L) {
                            platform.pauseCurrentThread(diff)
                        }
                        if (isRunning.get()) {
                            val tick = TimerTick(
                                    counter = counter,
                                    time = counter * interval,
                                    isLast = counter == limit.get()
                            )
                            timerEvent.sendEvent(tick)
                        }
                        counter++;
                        val limit = limit.get()
                        if (limit >= 0 && counter > limit) {
                            isRunning.set(false)
                        }
                    }
                }
            }
        }
        component.getLifecycle().onExitFromActiveStage {
            isRunning.set(false)
        }
    }

    fun setLimit(limit: Long) {
        this.limit.set(limit)
    }

    fun start() {
        isRunning.set(true)
        component.getLifecycle().onExitFromActiveStage {
            stop()
        }
    }

    fun start(interval: Long) {
        this.interval.set(interval)
        start()
    }

    fun stop() {
        isRunning.set(false)
    }

    fun observe(observer: Observer<TimerTick>) {
        timerEvent.subscribe(observer)
    }

    fun observe(action: (TimerTick) -> Unit) {
        timerEvent.subscribe(action)
    }

    fun observe(): Entity<TimerTick> {
        return timerEvent
    }

}