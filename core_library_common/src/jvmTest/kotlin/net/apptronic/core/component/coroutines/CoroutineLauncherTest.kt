package net.apptronic.core.component.coroutines

import net.apptronic.core.component.context.close
import net.apptronic.core.component.extensions.BaseComponent
import net.apptronic.core.component.lifecycle.enterStage
import net.apptronic.core.component.lifecycle.exitStage
import net.apptronic.core.testutils.TestLifecycle
import net.apptronic.core.testutils.testContext
import org.junit.Test

class CoroutineLauncherTest {

    val component = BaseComponent(testContext())

    @Test
    fun shouldExecuteOnLocal() {
        val coroutineLauncher = component.coroutineLaunchers().local
        var invoked = false
        coroutineLauncher.launch {
            invoked = true
        }
        assert(invoked)
    }

    @Test
    fun shouldNotExecuteOnLocal() {
        val coroutineLauncher = component.coroutineLaunchers().local
        component.context.close()
        var invoked = false
        coroutineLauncher.launch {
            invoked = true
        }
        assert(!invoked)
    }

    @Test
    fun shouldExecuteOnScoped() {
        val coroutineLauncher = component.coroutineLaunchers().scoped
        var invoked = false
        coroutineLauncher.launch {
            invoked = true
        }
        assert(invoked)
    }

    @Test
    fun shouldNotExecuteOnScoped() {
        val coroutineLauncher = component.coroutineLaunchers().scoped
        component.context.close()
        var invoked = false
        coroutineLauncher.launch {
            invoked = true
        }
        assert(!invoked)
    }

    @Test
    fun shouldExecuteOnlyUntilExit() {
        enterStage(component.context, TestLifecycle.STAGE_CREATED)
        val coroutineLauncher = component.coroutineLaunchers().scoped
        var invocations = 0
        coroutineLauncher.launch {
            invocations++
        }
        assert(invocations == 1)

        // coroutineLauncher cancelled after stage was exited
        exitStage(component.context, TestLifecycle.STAGE_CREATED)
        coroutineLauncher.launch {
            invocations++
        }
        assert(invocations == 1)

        // coroutineLauncher not resumes after stage was re-entered
        enterStage(component.context, TestLifecycle.STAGE_CREATED)
        coroutineLauncher.launch {
            invocations++
        }
        assert(invocations == 1)
    }

}