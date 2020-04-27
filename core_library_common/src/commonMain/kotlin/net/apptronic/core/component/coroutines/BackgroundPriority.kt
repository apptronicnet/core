package net.apptronic.core.component.coroutines

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

const val PRIORITY_LOWEST = 100000
const val PRIORITY_LOW = 10000
const val PRIORITY_MEDIUM = 1000
const val PRIORITY_HIGH = 100
const val PRIORITY_HIGHEST = 10
const val PRIORITY_URGENT = 1

fun backgroundPriority(priority: Int): CoroutineContext {
    return BackgroundDispatcher + CoroutineContextPriority(priority)
}

private object CoroutinePriorityKey : CoroutineContext.Key<CoroutineContextPriority>

private class CoroutineContextPriority(
        val priority: Int
) : CoroutineContext.Element {

    override val key: CoroutineContext.Key<*>
        get() = CoroutinePriorityKey

}

private val BackgroundDispatcher: CoroutineDispatcher = BackgroundDispatcherImpl()

private class BackgroundDispatcherImpl : CoroutineDispatcher() {

    private val internalScope = CoroutineScope(CoroutineName("BackgroundDispatcher"))

    private val maxThreads = 2
    private var running = 0

    private val targetDispatcher = Dispatchers.Default

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
                internalScope.launch(Dispatchers.Main) {
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
        internalScope.launch(Dispatchers.Main) {
            val priorityContext = context[CoroutinePriorityKey]
            val priority = priorityContext?.priority ?: PRIORITY_MEDIUM
            coroutineQueue.add(CoroutineBlock(context, priority, block))
            coroutineQueue.sort()
        }
    }

    private fun dispatchNext() {
        while (running < maxThreads && coroutineQueue.isNotEmpty()) {
            coroutineQueue.removeAt(0).dispatch()
        }
    }

}