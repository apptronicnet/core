package net.apptronic.core.entity.timer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.apptronic.core.base.elapsedRealtimeMillis
import net.apptronic.core.base.subject.PublishSubject
import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.coroutines.createLifecycleCoroutineScope
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.ObservableEntity
import net.apptronic.core.entity.commons.value
import net.apptronic.core.entity.timer.TimerEntity.Companion.INFINITE

fun Contextual.timerEntity(
    interval: Long,
    limit: Long = TimerEntity.INFINITE,
    action: ((TimerTick) -> Unit)? = null
): TimerEntity {
    return TimerEntityImpl(context, initialInterval = interval, initialLimit = limit).apply {
        if (action != null) {
            subscribe {
                action(it)
            }
        }
    }
}

interface TimerEntity : Entity<TimerTick> {

    var limit: Long

    var interval: Long

    val isRunning: Boolean

    fun start()

    fun start(interval: Long)

    fun stop()

    companion object {
        val INFINITE = -1L
    }

}

private class TimerEntityImpl internal constructor(
    override val context: Context,
    initialInterval: Long,
    initialLimit: Long
) : ObservableEntity<TimerTick>(), TimerEntity {

    private val isRunningValue = context.value(false)

    override val observable = PublishSubject<TimerTick>()
    private var coroutineScope: CoroutineScope? = null

    init {
        isRunningValue.subscribe(context) {
            if (it) {
                val coroutineScope = context.createLifecycleCoroutineScope()
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
            isRunningValue.set(false)
        }
    }

    private suspend fun runTimer() {
        observable.update(
            TimerTick(
                counter = 0,
                time = 0L,
                isLast = (limit <= 0L && limit != INFINITE) || interval <= 0L
            )
        )
        if (interval <= 0) {
            isRunningValue.set(false)
        }
        if (limit == 0L) {
            isRunningValue.set(false)
        }
        val start = elapsedRealtimeMillis()
        var counter = 1L
        var next: Long = start + interval
        while (isRunningValue.get()) {
            val current = elapsedRealtimeMillis() - start
            val diff = next - current
            if (diff > 0L) {
                delay(diff)
            }
            if (isRunningValue.get()) {
                observable.update(
                    TimerTick(
                        counter = counter,
                        time = next - start,
                        isLast = counter == limit
                    )
                )
            }
            counter++
            if (interval <= 0) {
                isRunningValue.set(false)
            } else {
                next += interval
                if (limit >= 0 && counter > limit) {
                    isRunningValue.set(false)
                }
            }
        }
    }

    override val isRunning: Boolean
        get() = isRunningValue.get()

    override var limit: Long = initialLimit

    override var interval: Long = initialInterval

    override fun start() {
        isRunningValue.set(true)
        context.lifecycle.onExitFromActiveStage {
            stop()
        }
    }

    override fun start(interval: Long) {
        this.interval = interval
        start()
    }

    override fun stop() {
        isRunningValue.set(false)
    }

}