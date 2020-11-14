package net.apptronic.core.commons.timer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.apptronic.core.base.elapsedRealtimeMillis
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.component.Component
import net.apptronic.core.context.coroutines.createContextCoroutineScope
import net.apptronic.core.context.coroutines.createLifecycleCoroutineScope
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.commons.typedEvent
import net.apptronic.core.entity.commons.value

fun Contextual.timer(
        interval: Long,
        limit: Long = Timer.INFINITE,
        action: ((TimerTick) -> Unit)? = null
): Timer {
    return Timer(context, initialInterval = interval, initialLimit = limit).also {
        if (action != null) {
            it.observe(action)
        }
    }
}

class Timer internal constructor(
        context: Context,
        initialInterval: Long,
        initialLimit: Long = INFINITE,
        private val scopedToStage: Boolean = true
) : Component(context) {

    companion object {
        val INFINITE = -1L
    }

    private val isRunning = value(false)
    private val limit = value(initialLimit)
    private val interval = value(initialInterval)

    private val timerEvent = typedEvent<TimerTick>()
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
                        isFirst = counter == 1L,
                        isLast = counter == limit.get()
                )
                timerEvent.update(tick)
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