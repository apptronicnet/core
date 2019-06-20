package net.apptronic.core.component

import net.apptronic.core.testutils.BaseTestComponent
import net.apptronic.core.testutils.TestLifecycle
import org.junit.Test
import kotlin.test.assertEquals

class InlinedLifecycleTest {

    private class TestComponent : BaseTestComponent() {

        var countOnStart = 0
        var countOnStop = 0

        var started = 0

    }

    private val component = TestComponent()
    private val lifecycle = component.getLifecycle()

    @Test
    fun shouldCallSomeTimes() {
        component.apply {
            doOnCreate {
                doOnActivated {
                    countOnStart++
                    onExit {
                        countOnStop++
                    }
                }
            }
        }
        lifecycle.enterStage(TestLifecycle.STAGE_CREATED)
        lifecycle.enterStage(TestLifecycle.STAGE_ACTIVATED)
        assertEquals(component.countOnStart, 1)
        assertEquals(component.countOnStop, 0)
        lifecycle.exitStage(TestLifecycle.STAGE_ACTIVATED)
        assertEquals(component.countOnStart, 1)
        assertEquals(component.countOnStop, 1)

        lifecycle.enterStage(TestLifecycle.STAGE_ACTIVATED)
        assertEquals(component.countOnStart, 2)
        assertEquals(component.countOnStop, 1)
        lifecycle.exitStage(TestLifecycle.STAGE_ACTIVATED)
        assertEquals(component.countOnStart, 2)
        assertEquals(component.countOnStop, 2)

        lifecycle.enterStage(TestLifecycle.STAGE_ACTIVATED)
        assertEquals(component.countOnStart, 3)
        assertEquals(component.countOnStop, 2)
        lifecycle.exitStage(TestLifecycle.STAGE_ACTIVATED)
        assertEquals(component.countOnStart, 3)
        assertEquals(component.countOnStop, 3)

        lifecycle.enterStage(TestLifecycle.STAGE_ACTIVATED)
        assertEquals(component.countOnStart, 4)
        assertEquals(component.countOnStop, 3)
        lifecycle.exitStage(TestLifecycle.STAGE_ACTIVATED)
        assertEquals(component.countOnStart, 4)
        assertEquals(component.countOnStop, 4)

        // doOnStart was called from doOnCreate so after doOnDestroy and reCreate it resubscribes
        lifecycle.exitStage(TestLifecycle.STAGE_CREATED)
        lifecycle.enterStage(TestLifecycle.STAGE_CREATED)

        lifecycle.enterStage(TestLifecycle.STAGE_ACTIVATED)
        assertEquals(component.countOnStart, 5)
        assertEquals(component.countOnStop, 4)
        lifecycle.exitStage(TestLifecycle.STAGE_ACTIVATED)
        assertEquals(component.countOnStart, 5)
        assertEquals(component.countOnStop, 5)

    }

    @Test
    fun shouldBeConsistent() {
        component.apply {
            doOnCreate {
                doOnActivated {
                    started++
                }
                doOnDeactivated {
                    started--
                }
            }
        }

        // first cycle
        lifecycle.enterStage(TestLifecycle.STAGE_CREATED)

        // start 1
        lifecycle.enterStage(TestLifecycle.STAGE_ACTIVATED)
        assertEquals(component.started, 1)
        lifecycle.exitStage(TestLifecycle.STAGE_ACTIVATED)
        assertEquals(component.started, 0)

        // start 2
        lifecycle.enterStage(TestLifecycle.STAGE_ACTIVATED)
        assertEquals(component.started, 1)
        lifecycle.exitStage(TestLifecycle.STAGE_ACTIVATED)
        assertEquals(component.started, 0)

        // now reenter parent stage
        lifecycle.exitStage(TestLifecycle.STAGE_CREATED)
        lifecycle.enterStage(TestLifecycle.STAGE_CREATED)

        // start 3
        lifecycle.enterStage(TestLifecycle.STAGE_ACTIVATED)
        assertEquals(component.started, 1)
        lifecycle.exitStage(TestLifecycle.STAGE_ACTIVATED)
        assertEquals(component.started, 0)

        // start 4
        lifecycle.enterStage(TestLifecycle.STAGE_ACTIVATED)
        assertEquals(component.started, 1)
        lifecycle.exitStage(TestLifecycle.STAGE_ACTIVATED)
        assertEquals(component.started, 0)

    }

}