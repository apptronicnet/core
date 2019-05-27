package net.apptronic.core.component.task

import net.apptronic.core.base.utils.BaseTestComponent
import net.apptronic.core.component.assert
import net.apptronic.core.component.assertNoValue
import net.apptronic.core.component.assertTrue
import net.apptronic.core.component.entity.functions.variants.isEqualsTo
import org.junit.Test

class TestSimpleTaskChain : BaseTestComponent() {

    private lateinit var transformation: (Int) -> String
    private var finally = 0

    private val taskScheduler = taskScheduler<Int> {
        onStart().map {
            transformation.invoke(it)
        }.onNext {
            result.set(it)
        }.onError {
            error.set(it)
        }.doFinally {
            finally++
        }
    }

    private val result = value<String>()
    private val error = value<Exception>()

    @Test
    fun shouldReturnResult() {
        transformation = {
            it.toString()
        }
        taskScheduler.execute(1)
        (result isEqualsTo "1").assertTrue()
        error.assertNoValue()
        assert(finally == 1)
    }

    @Test
    fun shouldReturnError() {
        transformation = {
            throw IllegalArgumentException()
        }
        taskScheduler.execute(1)
        result.assertNoValue()
        error.assert { it is IllegalArgumentException }
        assert(finally == 1)
    }

}