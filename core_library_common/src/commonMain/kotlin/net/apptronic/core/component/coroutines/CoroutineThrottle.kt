package net.apptronic.core.component.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.apptronic.core.base.collections.LinkedQueue

fun CoroutineScope.throttler(size: Int = 1): CoroutineThrottler {
    return CoroutineThrottler(this, if (size > 1) size else 1)
}

/**
 * This class allows launch coroutine actions with throttling them. It means that next action will be executed
 * only after current is completed. Queue of action have specific limit and oldest action automatically removed from
 * queue if it's size exceeds limit.
 */
class CoroutineThrottler internal constructor(
        private val coroutineScope: CoroutineScope,
        private val size: Int
) {

    private var isRunning: Boolean = false
    private var queue = LinkedQueue<suspend CoroutineScope.() -> Unit>()

    fun launch(block: suspend CoroutineScope.() -> Unit) {
        queue.add(block)
        queue.trim(size)
        launchNext()
    }

    private fun launchNext() {
        if (!isRunning) {
            coroutineScope.launch {
                isRunning = true
                do {
                    val next = queue.take()
                    next?.invoke(this)
                } while (next != null)
                isRunning = false
            }
        }
    }

}