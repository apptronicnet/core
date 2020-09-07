package net.apptronic.core.component.coroutines

import kotlinx.coroutines.*
import org.junit.Test
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

class BackgroundPriorityTest {

    private val single = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    private val jobs = mutableListOf<Job>()
    private val executionQueue = mutableListOf<String>()
    private val scope = CoroutineScope(CoroutineName("Test") + Dispatchers.Unconfined + Job())

    private fun dispatch(coroutineContext: CoroutineContext, name: String) {
        val job = scope.launch(coroutineContext) {
            delay(1)
            executionQueue.add(name)
        }
        jobs.add(job)
    }

    @Test
    fun shouldRunWithPriority() {
        val dispatcher = BackgroundPriorityDispatcher(single, Dispatchers.Default)
        val high = dispatcher.withPriority(PRIORITY_HIGH)
        val medium = dispatcher.withPriority(PRIORITY_MEDIUM)
        val low = dispatcher.withPriority(PRIORITY_LOW)

        dispatch(low, "Low-1")
        dispatch(low, "Low-2")
        dispatch(low, "Low-3")
        dispatch(low, "Low-4")
        dispatch(low, "Low-5")

        dispatch(medium, "Medium-1")
        dispatch(medium, "Medium-2")
        dispatch(medium, "Medium-3")

        dispatch(high, "High-1")
        dispatch(high, "High-2")
        dispatch(high, "High-3")

        runBlocking {
            jobs.forEach {
                it.join()
            }
        }

        executionQueue.forEach {
            println(it)
        }
        // first two was executed immediately as queue was not filled
        assert(executionQueue[0].startsWith("Low"))
        assert(executionQueue[1].startsWith("Low"))
        // when all next was scheduled in priority order
        assert(executionQueue[2].startsWith("High"))
        assert(executionQueue[3].startsWith("High"))
        assert(executionQueue[4].startsWith("High"))
        assert(executionQueue[5].startsWith("Medium"))
        assert(executionQueue[6].startsWith("Medium"))
        assert(executionQueue[7].startsWith("Medium"))
        assert(executionQueue[8].startsWith("Low"))
        assert(executionQueue[9].startsWith("Low"))
        assert(executionQueue[10].startsWith("Low"))
    }

}