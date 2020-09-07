package net.apptronic.core.component.coroutines

import kotlinx.coroutines.*
import net.apptronic.core.base.collections.queueOf
import kotlin.coroutines.CoroutineContext

/**
 * Create [CoroutineThrottler] which executes all coroutines one after another.
 */
fun CoroutineScope.serialThrottler(): CoroutineThrottler {
    return throttler(Int.MAX_VALUE)
}

/**
 * Create [CoroutineThrottler] which executes all coroutines one after another throttling it's maximum number
 * in queue to [size]. In case when number of items in queue exceeds this limit first will be thrown out.
 */
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

    private class Task(
            val coroutineContext: CoroutineContext,
            val start: CoroutineStart,
            val block: suspend CoroutineScope.() -> Unit
    ) {
        val deferred = CompletableDeferred<Unit>()
    }

    private var isRunning: Boolean = false
    private var queue = queueOf<Task>()

    fun launch(
            coroutineContext: CoroutineContext = coroutineScope.coroutineContext,
            start: CoroutineStart = CoroutineStart.DEFAULT,
            block: suspend CoroutineScope.() -> Unit
    ): Job {
        val task = Task(coroutineContext, start, block)
        queue.add(task)
        queue.trim(size) {
            task.deferred.completeExceptionally(CancellationException("Trimmed by CoroutineThrottler"))
        }
        launchNext()
        return task.deferred
    }

    private fun launchNext() {
        if (!isRunning) {
            val nextHolder = queue.take()
            if (nextHolder != null) {
                val next = nextHolder.value
                isRunning = true
                val job = coroutineScope.launch(next.coroutineContext, next.start, next.block)
                coroutineScope.launch {
                    try {
                        job.join()
                        next.deferred.complete(Unit)
                    } catch (e: Exception) {
                        next.deferred.completeExceptionally(e)
                    } finally {
                        isRunning = false
                        if (isActive) {
                            launchNext()
                        }
                    }
                }
            }
        }
    }

}