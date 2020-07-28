package net.apptronic.core.component.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import net.apptronic.core.component.context.terminate
import net.apptronic.core.component.lifecycle.Lifecycle
import net.apptronic.core.component.lifecycle.enterStage
import net.apptronic.core.component.lifecycle.exitStage
import net.apptronic.core.component.plugin.ExtensionDescriptor
import net.apptronic.core.testutils.TestLifecycle
import net.apptronic.core.testutils.testContext
import org.junit.Test
import java.util.concurrent.CancellationException
import kotlin.test.*

class CoroutineScopeInstancesTest {

    val context = testContext()

    @Test
    fun shouldExecuteOnLocal() {
        val coroutineScope = context.contextCoroutineScope
        var invoked = false
        coroutineScope.launch {
            invoked = true
        }
        assert(invoked)
    }

    @Test
    fun shouldNotExecuteOnLocal() {
        val coroutineScope = context.contextCoroutineScope
        context.terminate()
        var invoked = false
        coroutineScope.launch {
            invoked = true
        }
        assert(!invoked)
    }

    @Test
    fun shouldExecuteOnScoped() {
        val coroutineScope = context.lifecycleCoroutineScope
        var invoked = false
        coroutineScope.launch {
            invoked = true
        }
        assert(invoked)
    }

    @Test
    fun shouldNotExecuteOnScoped() {
        val coroutineScope = context.lifecycleCoroutineScope
        context.terminate()
        var invoked = false
        coroutineScope.launch {
            invoked = true
        }
        assert(!invoked)
    }

    @Test
    fun shouldExecuteOnlyUntilExit() {
        enterStage(context, TestLifecycle.STAGE_CREATED)
        assertEquals(context.lifecycle.getActiveStage()!!.getStageDefinition(), TestLifecycle.STAGE_CREATED)
        val coroutineScope = context.lifecycleCoroutineScope
        var invocations = 0
        coroutineScope.launch {
            invocations++
        }
        assert(invocations == 1)

        // coroutineScope cancelled after stage was exited
        exitStage(context, TestLifecycle.STAGE_CREATED)
        assertEquals(context.lifecycle.getActiveStage()!!.getStageDefinition(), Lifecycle.ROOT_STAGE)
        coroutineScope.launch {
            invocations++
        }
        assert(invocations == 1)

        // coroutineScope not resumes after stage was re-entered
        enterStage(context, TestLifecycle.STAGE_CREATED)
        assertEquals(context.lifecycle.getActiveStage()!!.getStageDefinition(), TestLifecycle.STAGE_CREATED)
        coroutineScope.launch {
            invocations++
        }
        assert(invocations == 1)
    }

    @Test
    fun shouldReturnSameInstanceOfScope() {
        enterStage(context, TestLifecycle.STAGE_CREATED)
        assertEquals(context.lifecycle.getActiveStage()!!.getStageDefinition(), TestLifecycle.STAGE_CREATED)
        val coroutineScope1 = context.lifecycleCoroutineScope
        val coroutineScope2 = context.lifecycleCoroutineScope
        assertSame(coroutineScope1, coroutineScope2)
    }

    @Test
    fun shouldReturnDifferentInstanceOfScope() {
        enterStage(context, TestLifecycle.STAGE_CREATED)
        assertEquals(context.lifecycle.getActiveStage()!!.getStageDefinition(), TestLifecycle.STAGE_CREATED)
        val coroutineScope1 = context.lifecycleCoroutineScope
        exitStage(context, TestLifecycle.STAGE_CREATED)
        assertEquals(context.lifecycle.getActiveStage()!!.getStageDefinition(), Lifecycle.ROOT_STAGE)
        enterStage(context, TestLifecycle.STAGE_CREATED)
        assertEquals(context.lifecycle.getActiveStage()!!.getStageDefinition(), TestLifecycle.STAGE_CREATED)
        val coroutineScope2 = context.lifecycleCoroutineScope
        assertNotSame(coroutineScope1, coroutineScope2)
    }

    @Test
    fun shouldReturnDifferentInstanceOfLifecycleStages() {
        enterStage(context, TestLifecycle.STAGE_CREATED)
        assertEquals(context.lifecycle.getActiveStage()!!.getStageDefinition(), TestLifecycle.STAGE_CREATED)
        val coroutineScope1 = context.lifecycleCoroutineScope
        enterStage(context, TestLifecycle.STAGE_WORKING)
        assertEquals(context.lifecycle.getActiveStage()!!.getStageDefinition(), TestLifecycle.STAGE_WORKING)
        val coroutineScope2 = context.lifecycleCoroutineScope
        assertNotSame(coroutineScope1, coroutineScope2)
    }

    @Test
    fun shouldRemoveExtension() {
        enterStage(context, TestLifecycle.STAGE_CREATED)
        val coroutineScope = context.lifecycleCoroutineScope
        val stage = context.lifecycle.getActiveStage()!!
        assertEquals(stage.getStageDefinition(), TestLifecycle.STAGE_CREATED)
        val clz = Class.forName("net.apptronic.core.component.coroutines.CoroutineScopeExtensionsKt")
        val field = clz.getDeclaredField("LifecycleStageScopeExtensionDescriptor")
        field.isAccessible = true
        val descriptor = field.get(null) as ExtensionDescriptor<CoroutineScope>
        assertNotNull(stage.extensions[descriptor])
        exitStage(context, TestLifecycle.STAGE_CREATED)
        assertNull(stage.extensions[descriptor])
    }

    @Test
    fun shouldReturnSameInstanceOfRootLifecycleAndContextScope() {
        enterStage(context, TestLifecycle.STAGE_CREATED)
        assertEquals(context.lifecycle.getActiveStage()!!.getStageDefinition(), TestLifecycle.STAGE_CREATED)
        val coroutineScope1 = context.contextCoroutineScope
        exitStage(context, TestLifecycle.STAGE_CREATED)
        assertEquals(context.lifecycle.getActiveStage()!!.getStageDefinition(), Lifecycle.ROOT_STAGE)
        val coroutineScope2 = context.lifecycleCoroutineScope
        assertSame(coroutineScope1, coroutineScope2)
    }

    @Test
    fun shouldReturnSameInstanceOfContextScope() {
        enterStage(context, TestLifecycle.STAGE_CREATED)
        assertEquals(context.lifecycle.getActiveStage()!!.getStageDefinition(), TestLifecycle.STAGE_CREATED)
        val coroutineScope1 = context.contextCoroutineScope
        exitStage(context, TestLifecycle.STAGE_CREATED)
        assertEquals(context.lifecycle.getActiveStage()!!.getStageDefinition(), Lifecycle.ROOT_STAGE)
        val coroutineScope2 = context.contextCoroutineScope
        assertSame(coroutineScope1, coroutineScope2)
    }

    @Test
    fun shouldIgnoreStagedCancellation() {
        enterStage(context, TestLifecycle.STAGE_CREATED)
        assertEquals(context.lifecycle.getActiveStage()!!.getStageDefinition(), TestLifecycle.STAGE_CREATED)
        val coroutineScope = context.lifecycleCoroutineScope
        coroutineScope.cancel(CancellationException("Try cancel"))
        var isInvoked = false
        coroutineScope.launch {
            isInvoked = true
        }
        assertTrue(isInvoked)
        assertTrue(coroutineScope.isActive)
    }

    @Test
    fun shouldIgnoreContextualCancellation() {
        enterStage(context, TestLifecycle.STAGE_CREATED)
        assertEquals(context.lifecycle.getActiveStage()!!.getStageDefinition(), TestLifecycle.STAGE_CREATED)
        val coroutineScope = context.contextCoroutineScope
        coroutineScope.cancel(CancellationException("Try cancel"))
        var isInvoked = false
        coroutineScope.launch {
            isInvoked = true
        }
        assertTrue(isInvoked)
        assertTrue(coroutineScope.isActive)
    }

}