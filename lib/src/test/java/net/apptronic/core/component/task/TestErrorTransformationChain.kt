package net.apptronic.core.component.task

import net.apptronic.core.base.utils.BaseTestComponent
import net.apptronic.core.component.assert
import net.apptronic.core.component.assertNoValue
import org.junit.Test

class TestErrorTransformationChain : BaseTestComponent() {

    private val result = value<Int>()
    private val error = value<Exception>()
    private var finally = 0
    private var onError = 0

    private val taskScheduler = taskScheduler<Int>(SchedulerMode.Parallel) {
        onStart().onNext {
            throw IllegalArgumentException()
        }.mapError {
            RuntimeException(it)
        }.onError {
            onError++
        }.sendResultTo(result)
            .sendErrorTo(error)
            .doFinally {
                finally++
            }
    }

    @Test
    fun verifyResults() {
        taskScheduler.execute(1)
        result.assertNoValue()
        error.assert {
            it is RuntimeException && it.cause is IllegalArgumentException
        }
        assert(finally == 1)
        assert(onError == 1)
    }

}