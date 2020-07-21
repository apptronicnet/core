package net.apptronic.core.component.timer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.apptronic.core.base.elapsedRealtimeMillis
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.coroutines.createContextCoroutineScope
import net.apptronic.core.component.coroutines.createLifecycleCoroutineScope
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.component.typedEvent
import net.apptronic.core.component.value

class Timer(
        private val context: Context,
        initialInterval: Long,
        initialLimit: Long = INFINITE,
        private val scopedToStage: Boolean = true
) {

    companion object {
        val INFINITE = -1L
    }

    private val isRunning = context.value(false)
    private val limit = context.value(initialLimit)
    private val interval = context.value(initialInterval)

    private val timerEvent = context.typedEvent<TimerTick>()
    private var coroutineScope: CoroutineScope? = null

    private fun createCoroutineScope(): CoroutineScope {
        return if (scopedToStage) {
            context.createLifecycleCoroutineScope()
        } else {
            context.createContextCoroutineScope()
        }
    }

    init {
        isRunning.subscribe(context) {
            if (it) {
                val coroutineScope = createCoroutineScope()
                this.coroutineScope = coroutineScope
                coroutineScope.launch {
                    runTimer()
                }
            } else {
                coroutineScope?.cancel("Timer stopped")
                coroutineScope = null
            }
        }
        context.lifecycle.onExitFromActiveStage {
            isRunning.set(false)
        }
    }

    private suspend fun runTimer() {
        val interval = interval.get()
        if (interval <= 0) {
            isRunning.set(false)
        }
        val start = elapsedRealtimeMillis()
        var counter = 1L;
        while (isRunning.get()) {
            val total = counter * interval
            val next = start + total
            val current = elapsedRealtimeMillis()
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
        if (scopedToStage) {
            context.lifecycle.onExitFromActiveStage {
                stop()
            }
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