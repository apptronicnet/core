package net.apptronic.core.component.timer

data class TimerTick(
        val counter: Long,
        val time: Long,
        val isLast: Boolean
)