package net.apptronic.core.component.task

import net.apptronic.core.base.utils.BaseTestComponent
import net.apptronic.core.component.assert
import net.apptronic.core.component.assertNoValue
import org.junit.Test

class TestNewTask : BaseTestComponent() {

    private val result = value<Int>()
    private val error = value<Exception>()
    private var finally = 0
    private var onError = 0

    @Test
    fun shouldBeSuccess() {
        newTask(200) {
            map {
                it.toString()
            }.map {
                it.length
            }.sendResultTo(result).sendErrorTo(error).doFinally {
                finally++
            }
        }
        error.assertNoValue()
        assert(finally == 1)
    }

    @Test
    fun shouldReturnError() {
        newTask(200) {
            map {
                it.toString()
            }.map {
                throw IllegalArgumentException()
            }.onError {
                onError++
            }.sendResultTo(result).sendErrorTo(error).doFinally {
                finally++
            }
        }
        result.assertNoValue()
        error.assert {
            it is IllegalArgumentException
        }
        assert(finally == 1)
        assert(onError == 1)
    }

    @Test(expected = TaskErrorIsNotHandledException::class)
    fun shouldThrowException() {
        newTask(200) {
            map {
                throw IllegalArgumentException()
            }
        }
    }

}