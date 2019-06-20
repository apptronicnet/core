package net.apptronic.core.component.task

import net.apptronic.core.testutils.BaseTestComponent
import net.apptronic.core.testutils.addDeferredWorker
import net.apptronic.core.component.assertHasValue
import net.apptronic.core.component.assertNoValue
import net.apptronic.core.threading.defineWorker
import org.junit.Test

private val DEFERRED_WORKER = defineWorker("DEFERRED")

class TestInterruptionOutsideChain : BaseTestComponent() {

    private val deferredWorker = addDeferredWorker(DEFERRED_WORKER)

    private val result = value<Int>()
    private val error = value<Exception>()
    private val interruptedException = value<TaskInterruptedException>()
    private var finally = 0

    private val taskScheduler = taskScheduler<Int>(SchedulerMode.Parallel) {
        onStart().onNext {
            it.toString()
        }.switchWorker(DEFERRED_WORKER).map {
            result.set(it)
        }.onError {
            throw RuntimeException()
        }.onInterrupted { taskInterruptedException ->
            interruptedException.set(taskInterruptedException)
        }.doFinally {
            finally++
        }
    }

    @Test
    fun verifyResults() {
        val task = taskScheduler.execute(1)
        result.assertNoValue()
        error.assertNoValue()

        task.interrupt()
        deferredWorker.runDeferredActions()

        result.assertNoValue()
        error.assertNoValue()

        interruptedException.assertHasValue()
        assert(finally == 1)
    }

}