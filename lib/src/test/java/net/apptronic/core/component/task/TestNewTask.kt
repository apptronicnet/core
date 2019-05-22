package net.apptronic.core.component.task

import net.apptronic.core.base.utils.BaseTestComponent
import net.apptronic.core.component.assert
import net.apptronic.core.component.assertNoValue
import net.apptronic.core.component.assertTrue
import net.apptronic.core.component.entity.functions.variants.isEqualsTo
import org.junit.Test

class TestNewTask : BaseTestComponent() {

    private val result = value<Int>()
    private val error = value<Exception>()
    private var finally = 0

    @Test
    fun shouldBeSuccess() {
        newTask(200).map {
            it.toString()
        }.map {
            it.length
        }.sendResultTo(result).sendErrorTo(error).doFinally {
            finally++
        }
        (result isEqualsTo 3).assertTrue()
        error.assertNoValue()
        assert(finally == 1)
    }

    @Test
    fun shouldReturnError() {
        newTask(200).map {
            it.toString()
        }.map {
            throw IllegalArgumentException()
        }.sendResultTo(result).sendErrorTo(error).doFinally {
            finally++
        }
        result.assertNoValue()
        error.assert {
            it is IllegalArgumentException
        }
        assert(finally == 1)
    }

}