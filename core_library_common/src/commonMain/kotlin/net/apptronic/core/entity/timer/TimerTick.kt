package net.apptronic.core.entity.timer

data class TimerTick(
    val counter: Long,
    val time: Long,
    val isLast: Boolean
) {
    val isFirst: Boolean = counter == 0L
}