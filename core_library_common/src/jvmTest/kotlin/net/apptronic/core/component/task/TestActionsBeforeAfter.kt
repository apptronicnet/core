package net.apptronic.core.component.task

import net.apptronic.core.testutils.BaseTestComponent
import net.apptronic.core.testutils.addDeferredWorker
import net.apptronic.core.threading.defineWorker
import org.junit.Test

private val DEFERRED_WORKER = defineWorker("DEFERRED")

class TestActionsBeforeAfter : BaseTestComponent() {

    private val deferredWorker = addDeferredWorker(DEFERRED_WORKER)

    private var beforeStart = 0
    private var onStart = 0
    private var onEnd = 0
    private var afterEnd = 0

    private val taskScheduler = taskScheduler<Int> {
        onBeforeProcessing {
            beforeStart++
        }
        onBeforeTask {
            onStart++
        }
        onStart(DEFERRED_WORKER).onNext {
            it.toString()
        }
        onAfterTask {
            onEnd++
        }
        onAfterProcessing {
            afterEnd++
        }
    }

    @Test
    fun verifySingleTask() {
        taskScheduler.execute(1)
        assert(beforeStart == 1)
        assert(onStart == 1)
        assert(onEnd == 0)
        assert(afterEnd == 0)

        deferredWorker.runDeferredActions()
        assert(beforeStart == 1)
        assert(onStart == 1)
        assert(onEnd == 1)
        assert(afterEnd == 1)
    }

    @Test
    fun verifyManyTasks() {
        taskScheduler.execute(1)
        assert(beforeStart == 1)
        assert(onStart == 1)
        assert(onEnd == 0)
        assert(afterEnd == 0)

        taskScheduler.execute(2)
        deferredWorker.runDeferredActions()

        assert(beforeStart == 1)
        assert(onStart == 2)
        assert(onEnd == 2)
        assert(afterEnd == 1)
    }

}