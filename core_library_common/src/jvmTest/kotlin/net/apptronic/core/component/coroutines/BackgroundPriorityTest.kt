package net.apptronic.core.component.coroutines

import kotlinx.coroutines.*
import org.junit.Test
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

class BackgroundPriorityTest {

    private val single = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    private val jobs = mutableListOf<Job>()
    private val deferreds = mutableListOf<CompletableDeferred<Unit>>()
    private val executionQueue = mutableListOf<String>()
    private val baseDispatcher = SynchronousDispatcher()
    private val scope = CoroutineScope(CoroutineName("Test") + baseDispatcher + Job())

    private fun dispatch(coroutineContext: CoroutineContext, name: String) {
        val deferred = CompletableDeferred<Unit>()
        deferreds.add(deferred)
        val job = scope.launch(coroutineContext) {
            deferred.await()
            executionQueue.add(name)
        }
        jobs.add(job)
    }

    @Test(timeout = 250L)
    fun shouldRunWithPriority() {
         val dispatcher = BackgroundPriorityDispatcher(single, baseDispatcher)
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

        deferreds.forEach {
            it.complete(Unit)
        }

        runBlocking {
            jobs.forEach {
                it.join()
            }
        }

        executionQueue.forEach {
            println(it)
        }
        assert(executionQueue[2].startsWith("High"))
        assert(executionQueue[5].startsWith("Medium"))
        assert(executionQueue[8].startsWith("Low"))
        assert(executionQueue[9].startsWith("Low"))
        assert(executionQueue[10].startsWith("Low"))
    }

}