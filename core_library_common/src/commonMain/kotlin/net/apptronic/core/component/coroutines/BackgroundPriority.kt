package net.apptronic.core.component.coroutines

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

const val PRIORITY_LOWEST = 100000
const val PRIORITY_LOW = 10000
const val PRIORITY_MEDIUM = 1000
const val PRIORITY_HIGH = 100
const val PRIORITY_HIGHEST = 10
const val PRIORITY_URGENT = 1

fun CoroutineDispatcher.withPriority(priority: Int): CoroutineContext {
    return this + CoroutineContextPriority(priority)
}

private object CoroutinePriorityKey : CoroutineContext.Key<CoroutineContextPriority>

private class CoroutineContextPriority(
        val priority: Int
) : CoroutineContext.Element {

    override val key: CoroutineContext.Key<*>
        get() = CoroutinePriorityKey

}

class BackgroundPriorityDispatcher(
        private val schedulerDispatcher: CoroutineDispatcher,
        private val targetDispatcher: CoroutineDispatcher,
        private val maxThreads: Int = 2
) : CoroutineDispatcher() {

    private val internalScope = CoroutineScope(CoroutineName("BackgroundDispatcher") + schedulerDispatcher + Job())

    private var running = 0

    inner class CoroutineBlock(
            private val context: CoroutineContext,
            private val priority: Int,
            private val block: Runnable
    ) : Comparable<CoroutineBlock>, Runnable {

        override fun compareTo(other: CoroutineBlock): Int {
            return priority.compareTo(other.priority)
        }

        override fun run() {
            try {
                block.run()
            } finally {
                internalScope.launch {
                    running--
                    dispatchNext()
                }
            }
        }

        fun dispatch() {
            running++
            targetDispatcher.dispatch(context, this)
        }

    }

    private val coroutineQueue = mutableListOf<CoroutineBlock>()

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        internalScope.launch(schedulerDispatcher) {
            val priorityContext = context[CoroutinePriorityKey]
            val priority = priorityContext?.priority ?: PRIORITY_MEDIUM
            coroutineQueue.add(CoroutineBlock(context, priority, block))
            coroutineQueue.sort()
            dispatchNext()
        }
    }

    private fun dispatchNext() {
        while (running < maxThreads && coroutineQueue.isNotEmpty()) {
            coroutineQueue.removeAt(0).dispatch()
        }
    }

}