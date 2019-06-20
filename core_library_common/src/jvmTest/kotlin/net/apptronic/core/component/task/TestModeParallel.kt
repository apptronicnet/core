package net.apptronic.core.component.task

import net.apptronic.core.testutils.BaseTestComponent
import net.apptronic.core.testutils.addDeferredWorker
import net.apptronic.core.threading.defineWorker
import org.junit.Test

private val DEFERRED_WORKER = defineWorker("DEFERRED")

class TestModeParallel : BaseTestComponent() {

    private val deferredWorker = addDeferredWorker(DEFERRED_WORKER)

    private val preProcessedItems = mutableListOf<Int>()
    private val postProcessedItems = mutableListOf<Int>()

    private val taskScheduler = taskScheduler<Int>(SchedulerMode.Parallel) {
        onStart().onNext {
            preProcessedItems.add(it)
        }.switchWorker(DEFERRED_WORKER).onNext {
            postProcessedItems.add(it)
        }
    }

    private fun assertResults(list: List<Int>, vararg results: Int) {
        assert(list.size == results.size)
        assert(list.toTypedArray().contentDeepEquals(results.toTypedArray()))
    }

    @Test
    fun verifyParallel() {
        taskScheduler.execute(1)
        taskScheduler.execute(2)
        taskScheduler.execute(3)

        assertResults(preProcessedItems, 1, 2, 3)
        assertResults(postProcessedItems)

        deferredWorker.runDeferredActions()

        assertResults(preProcessedItems, 1, 2, 3)
        assertResults(postProcessedItems, 1, 2, 3)
    }

}