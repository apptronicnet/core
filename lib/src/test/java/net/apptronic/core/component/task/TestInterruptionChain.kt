package net.apptronic.core.component.task

import net.apptronic.core.base.utils.BaseTestComponent
import net.apptronic.core.component.assertHasValue
import net.apptronic.core.component.assertNoValue
import net.apptronic.core.component.assertTrue
import net.apptronic.core.component.entity.functions.variants.isEqualsTo
import org.junit.Test

class TestInterruptionChain : BaseTestComponent() {

    private val result = value<Int>()
    private val error = value<Exception>()
    private val interruptedTask = value<Task>()
    private val interruptedException = value<TaskInterruptedException>()
    private var finally = 0

    private val taskScheduler = taskScheduler<Int>(SchedulerMode.Parallel) {
        onStart().onNext {
            it.toString()
        }.map {
            throw TaskInterruptedException()
        }.sendResultTo(result)
            .sendErrorTo(error)
            .doFinally {
                finally++
            }.onInterrupted { task, taskInterruptedException ->
                interruptedTask.set(task)
                interruptedException.set(taskInterruptedException)
            }
    }

    @Test
    fun verifyResults() {
        val task = taskScheduler.execute(1)

        result.assertNoValue()
        error.assertNoValue()
        (interruptedTask isEqualsTo task).assertTrue()
        interruptedException.assertHasValue()
        assert(finally == 1)
    }

}