package net.apptronic.core.component.task

import net.apptronic.core.base.utils.BaseTestComponent
import net.apptronic.core.base.utils.TestWorker
import org.junit.Test

class TestNoHandledError : BaseTestComponent() {

    private val taskScheduler = taskScheduler<Int> {
        onStart(TestWorker).map {
            throw RuntimeException()
        }
    }

    @Test(expected = TaskErrorIsNotHandledException::class)
    fun verifyResults() {
        taskScheduler.execute(1)
    }

}