package net.apptronic.core.commons.timer

data class TimerTick(
        val counter: Long,
        val time: Long,
        val isFirst: Boolean,
        val isLast: Boolean
)