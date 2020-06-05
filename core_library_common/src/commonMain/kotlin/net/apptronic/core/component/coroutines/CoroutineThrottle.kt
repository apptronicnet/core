package net.apptronic.core.component.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import net.apptronic.core.base.collections.LinkedQueue
import net.apptronic.core.component.context.Context
import kotlin.coroutines.CoroutineContext

/**
 * Create [CoroutineLauncher] which executes all coroutines one after another.
 */
fun CoroutineLauncher.debouncer(): CoroutineLauncher {
    return throttler(Int.MAX_VALUE)
}

/**
 * Create [CoroutineLauncher] which executes all coroutines one after another throttling it'x maximum number in queue to
 * [size]
 */
fun CoroutineLauncher.throttler(size: Int = 1): CoroutineLauncher {
    return CoroutineThrottler(this, if (size > 1) size else 1)
}

/**
 * This class allows launch coroutine actions with throttling them. It means that next action will be executed
 * only after current is completed. Queue of action have specific limit and oldest action automatically removed from
 * queue if it's size exceeds limit.
 */
class CoroutineThrottler internal constructor(
        private val target: CoroutineLauncher,
        private val size: Int
) : CoroutineLauncher {

    override val context: Context = target.context
    override val coroutineScope: CoroutineScope = target.coroutineScope

    private class Task(
            val coroutineContext: CoroutineContext,
            val start: CoroutineStart,
            val block: suspend CoroutineScope.() -> Unit
    )

    private var isRunning: Boolean = false
    private var queue = LinkedQueue<Task>()

    override fun launch(coroutineContext: CoroutineContext, start: CoroutineStart, block: suspend CoroutineScope.() -> Unit) {
        queue.add(Task(coroutineContext, start, block))
        queue.trim(size)
        launchNext()
    }

    private fun launchNext() {
        if (!isRunning) {
            isRunning = true
            val next = queue.take()
            if (next != null) {
                val job = coroutineScope.launch(next.coroutineContext, next.start, next.block)
                target.launch {
                    job.join()
                    isRunning = false
                    launchNext()
                }
            }
        }
    }

}