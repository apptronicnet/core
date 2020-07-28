package net.apptronic.core.component.coroutines

import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import net.apptronic.core.component.context.terminate
import net.apptronic.core.component.lifecycle.Lifecycle
import net.apptronic.core.component.lifecycle.enterStage
import net.apptronic.core.component.lifecycle.exitStage
import net.apptronic.core.testutils.TestLifecycle
import net.apptronic.core.testutils.testContext
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotSame

class CoroutineScopeCreationsTest {

    val context = testContext()

    @Test
    fun shouldExecuteOnLocal() {
        val coroutineScope = context.createContextCoroutineScope()
        var invoked = false
        coroutineScope.launch {
            invoked = true
        }
        assert(invoked)
    }

    @Test
    fun shouldNotExecuteOnLocal() {
        val coroutineScope = context.createContextCoroutineScope()
        context.terminate()
        var invoked = false
        coroutineScope.launch {
            invoked = true
        }
        assert(!invoked)
    }

    @Test
    fun shouldNotExecuteOnLocalCancelled() {
        val coroutineScope = context.createContextCoroutineScope()
        coroutineScope.cancel()
        var invoked = false
        coroutineScope.launch {
            invoked = true
        }
        assert(!invoked)
    }

    @Test
    fun shouldExecuteOnLifecycle() {
        enterStage(context, TestLifecycle.STAGE_CREATED)
        assertEquals(context.lifecycle.getActiveStage()!!.getStageDefinition(), TestLifecycle.STAGE_CREATED)
        val coroutineScope = context.createLifecycleCoroutineScope()
        var invoked = false
        coroutineScope.launch {
            invoked = true
        }
        assert(invoked)
    }

    @Test
    fun shouldNotExecuteOnLifecycle() {
        enterStage(context, TestLifecycle.STAGE_CREATED)
        assertEquals(context.lifecycle.getActiveStage()!!.getStageDefinition(), TestLifecycle.STAGE_CREATED)
        val coroutineScope = context.createLifecycleCoroutineScope()
        exitStage(context, TestLifecycle.STAGE_CREATED)
        assertEquals(context.lifecycle.getActiveStage()!!.getStageDefinition(), Lifecycle.ROOT_STAGE)
        var invoked = false
        coroutineScope.launch {
            invoked = true
        }
        assert(!invoked)
    }

    @Test
    fun shouldNotExecuteOnLifecycleCancelled() {
        enterStage(context, TestLifecycle.STAGE_CREATED)
        assertEquals(context.lifecycle.getActiveStage()!!.getStageDefinition(), TestLifecycle.STAGE_CREATED)
        val coroutineScope = context.createLifecycleCoroutineScope()
        coroutineScope.cancel()
        var invoked = false
        coroutineScope.launch {
            invoked = true
        }
        assert(!invoked)
    }

    @Test
    fun shouldReturnDifferentInstancesContext() {
        val coroutineScope1 = context.createContextCoroutineScope()
        val coroutineScope2 = context.createContextCoroutineScope()
        assertNotSame(coroutineScope1, coroutineScope2)
    }

    @Test
    fun shouldReturnDifferentInstancesContextOnStages() {
        val coroutineScope1 = context.createContextCoroutineScope()
        enterStage(context, TestLifecycle.STAGE_CREATED)
        assertEquals(context.lifecycle.getActiveStage()!!.getStageDefinition(), TestLifecycle.STAGE_CREATED)
        val coroutineScope2 = context.createContextCoroutineScope()
        assertNotSame(coroutineScope1, coroutineScope2)
    }

    @Test
    fun shouldReturnDifferentInstancesLifecycle() {
        val coroutineScope1 = context.createLifecycleCoroutineScope()
        val coroutineScope2 = context.createLifecycleCoroutineScope()
        assertNotSame(coroutineScope1, coroutineScope2)
    }

    @Test
    fun shouldReturnDifferentInstancesLifecycleStages() {
        val coroutineScope1 = context.createLifecycleCoroutineScope()
        enterStage(context, TestLifecycle.STAGE_CREATED)
        assertEquals(context.lifecycle.getActiveStage()!!.getStageDefinition(), TestLifecycle.STAGE_CREATED)
        val coroutineScope2 = context.createLifecycleCoroutineScope()
        assertNotSame(coroutineScope1, coroutineScope2)
    }

    @Test
    fun shouldReturnDifferentInstancesForContextAndLifecycle() {
        val coroutineScope1 = context.createContextCoroutineScope()
        val coroutineScope2 = context.createLifecycleCoroutineScope()
        assertNotSame(coroutineScope1, coroutineScope2)
    }

    @Test
    fun shouldNotReturnSameAsInstances1() {
        val coroutineScope1 = context.contextCoroutineScope
        val coroutineScope2 = context.createContextCoroutineScope()
        assertNotSame(coroutineScope1, coroutineScope2)
    }

    @Test
    fun shouldNotReturnSameAsInstances2() {
        val coroutineScope1 = context.lifecycleCoroutineScope
        val coroutineScope2 = context.createLifecycleCoroutineScope()
        assertNotSame(coroutineScope1, coroutineScope2)
    }

    @Test
    fun shouldNotReturnSameAsInstances3() {
        val coroutineScope1 = context.contextCoroutineScope
        val coroutineScope2 = context.createLifecycleCoroutineScope()
        assertNotSame(coroutineScope1, coroutineScope2)
    }

    @Test
    fun shouldNotReturnSameAsInstances4() {
        val coroutineScope1 = context.lifecycleCoroutineScope
        val coroutineScope2 = context.createContextCoroutineScope()
        assertNotSame(coroutineScope1, coroutineScope2)
    }

}