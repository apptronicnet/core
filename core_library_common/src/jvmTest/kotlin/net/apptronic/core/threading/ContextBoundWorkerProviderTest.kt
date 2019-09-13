package net.apptronic.core.threading

import net.apptronic.core.component.lifecycle.enterStage
import net.apptronic.core.component.lifecycle.exitStage
import net.apptronic.core.testutils.DeferredWorker
import net.apptronic.core.testutils.TestContext
import net.apptronic.core.testutils.TestLifecycle
import org.junit.Test

private val CONTEXT_BOUND_WORKER = defineWorker("CONTEXT_BOUND_WORKER")

class ContextBoundWorkerProviderTest : TestContext() {

    private val deferredWorker = DeferredWorker()

    init {
        getScheduler().assignWorker(CONTEXT_BOUND_WORKER, ContextBoundWorkerProvider(deferredWorker))
    }

    @Test
    fun shouldCall() {
        val contextBoundWorker = getScheduler().getWorker(CONTEXT_BOUND_WORKER)
        enterStage(this, TestLifecycle.STAGE_CREATED)

        var called = false
        contextBoundWorker.execute {
            called = true
        }
        assert(!called)
        deferredWorker.runDeferredActions()
        assert(called)

        var calledAfterExit = false
        contextBoundWorker.execute {
            calledAfterExit = true
        }
    }

    @Test
    fun shouldNotCallAfterExit() {
        val contextBoundWorker = getScheduler().getWorker(CONTEXT_BOUND_WORKER)
        enterStage(this, TestLifecycle.STAGE_CREATED)

        var called = false
        contextBoundWorker.execute {
            called = true
        }
        assert(!called)
        exitStage(this, TestLifecycle.STAGE_CREATED)
        deferredWorker.runDeferredActions()
        assert(!called)
    }

    @Test
    fun shouldNotCallAfterReEnter() {
        val contextBoundWorker = getScheduler().getWorker(CONTEXT_BOUND_WORKER)
        enterStage(this, TestLifecycle.STAGE_CREATED)

        var called = false
        contextBoundWorker.execute {
            called = true
        }
        assert(!called)
        exitStage(this, TestLifecycle.STAGE_CREATED)
        enterStage(this, TestLifecycle.STAGE_CREATED)
        deferredWorker.runDeferredActions()
        assert(!called)
    }

    @Test
    fun shouldNotCallAfterTerminate() {
        val contextBoundWorker = getScheduler().getWorker(CONTEXT_BOUND_WORKER)

        var called = false
        contextBoundWorker.execute {
            called = true
        }
        assert(!called)
        getLifecycle().terminate()
        deferredWorker.runDeferredActions()
        assert(!called)
    }

}