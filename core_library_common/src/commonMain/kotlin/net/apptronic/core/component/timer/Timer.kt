package net.apptronic.core.component.timer

import kotlinx.coroutines.delay
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.Component
import net.apptronic.core.component.coroutines.coroutineLauncherScoped
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.component.typedEvent
import net.apptronic.core.component.value
import net.apptronic.core.platform.getPlatform

class Timer(
        private val component: Component,
        initialInterval: Long,
        initialLimit: Long = INFINITE
) {

    companion object {
        val INFINITE = -1L
    }

    private val context = component.context
    private val coroutineLauncher = component.context.coroutineLauncherScoped()
    private val isRunning = component.value(false)
    private val limit = component.value(initialLimit)
    private val interval = component.value(initialInterval)

    private val timerEvent = component.typedEvent<TimerTick>()

    init {
        isRunning.subscribe {
            if (it) {
                coroutineLauncher.launch {
                    runTimer()
                }
            }
        }
        context.lifecycle.onExitFromActiveStage {
            isRunning.set(false)
        }
    }

    private suspend fun runTimer() {
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
                delay(diff)
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

    fun setLimit(limit: Long) {
        this.limit.set(limit)
    }

    fun start() {
        isRunning.set(true)
        context.lifecycle.onExitFromActiveStage {
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