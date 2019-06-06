package net.apptronic.core.component.task

import net.apptronic.core.base.utils.BaseTestComponent
import net.apptronic.core.base.utils.TestWorker
import net.apptronic.core.component.assertNoValue
import net.apptronic.core.component.assertTrue
import net.apptronic.core.component.entity.functions.isEqualsTo
import org.junit.Test

class TestTransformationChain : BaseTestComponent() {

    private val result = value<Int>()
    private val error = value<Exception>()
    private var finally = 0

    private val taskScheduler = taskScheduler<Int> {
        onStart(TestWorker).map {
            it.toString()
        }.map {
            it.length
        }.sendResultTo(result)
            .sendErrorTo(error).doFinally {
                finally++
            }
    }

    @Test
    fun verifyResults() {
        taskScheduler.execute(1)
        (result isEqualsTo 1).assertTrue()
        error.assertNoValue()
        assert(finally == 1)

        taskScheduler.execute(10)
        (result isEqualsTo 2).assertTrue()
        error.assertNoValue()
        assert(finally == 2)

        taskScheduler.execute(100)
        (result isEqualsTo 3).assertTrue()
        error.assertNoValue()
        assert(finally == 3)
    }

}