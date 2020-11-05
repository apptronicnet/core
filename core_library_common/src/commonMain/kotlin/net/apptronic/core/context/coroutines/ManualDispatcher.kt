package net.apptronic.core.context.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import kotlin.coroutines.CoroutineContext

/**
 * This dispatcher designed to execute all [Runnable] blocks only when explicitly called [ManualDispatcher.runAll]
 */
class ManualDispatcher : CoroutineDispatcher() {

    private val awaitingBlocks = mutableListOf<Runnable>()

    /**
     * Execute all blocks which was dispatched on this instance of [ManualDispatcher] synchronously from current thread.
     *
     * All blocks which are dispatched on this instance of [ManualDispatcher] while executing awaiting queue will also
     * be executed in order they are dispatched during this call.
     */
    fun runAll() {
        while (awaitingBlocks.isNotEmpty()) {
            awaitingBlocks.removeAt(0).run()
        }
    }

    /**
     * Execute all blocks which was dispatched on this instance of [ManualDispatcher] synchronously from current thread.
     *
     * All blocks which are dispatched on this instance of [ManualDispatcher] while executing awaiting queue will be
     * added to awaiting queue but not executed.
     */
    fun runAwaiting() {
        val toExecute = awaitingBlocks.toTypedArray()
        awaitingBlocks.clear()
        toExecute.forEach {
            it.run()
        }
    }

    /**
     * Add [block] to awaiting queue.
     */
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        awaitingBlocks.add(block)
    }

}