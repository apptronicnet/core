package net.apptronic.core.component.task

import net.apptronic.core.testutils.BaseTestComponent
import net.apptronic.core.component.assertHasValue
import net.apptronic.core.component.assertNoValue
import org.junit.Test

class TestInterruptionInsideChain : BaseTestComponent() {

    private val result = value<Int>()
    private val error = value<Exception>()
    private val interruptedException = value<TaskInterruptedException>()
    private var finally = 0

    private val taskScheduler = taskScheduler<Int>(SchedulerMode.Parallel) {
        onStart().onNext {
            it.toString()
        }.map {
            throw TaskInterruptedException()
        }.onNext {
            throw RuntimeException()
        }.sendResultTo(result)
            .sendErrorTo(error)
            .doFinally {
                finally++
            }.onInterrupted { taskInterruptedException ->
                interruptedException.set(taskInterruptedException)
            }
    }

    @Test
    fun verifyResults() {
        taskScheduler.execute(1)
        result.assertNoValue()
        error.assertNoValue()
        interruptedException.assertHasValue()
        assert(finally == 1)
    }

}