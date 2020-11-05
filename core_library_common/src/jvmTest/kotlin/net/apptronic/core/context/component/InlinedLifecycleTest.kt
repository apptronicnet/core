package net.apptronic.core.context.component

import net.apptronic.core.context.lifecycle.enterStage
import net.apptronic.core.context.lifecycle.exitStage
import net.apptronic.core.testutils.BaseTestComponent
import net.apptronic.core.testutils.TestLifecycle
import org.junit.Test
import kotlin.test.assertEquals

class InlinedLifecycleTest {

    private class TestComponent : BaseTestComponent() {

        var countOnCreate = 0
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
                countOnCreate++
                doOnActivated {
                    countOnStart++
                    onExit {
                        countOnStop++
                    }
                }
            }
        }
        component.enterStage(TestLifecycle.STAGE_CREATED)
        assertEquals(component.countOnCreate, 1)
        assertEquals(component.countOnStart, 0)
        assertEquals(component.countOnStop, 0)
        component.enterStage(TestLifecycle.STAGE_ACTIVATED)
        assertEquals(component.countOnCreate, 1)
        assertEquals(component.countOnStart, 1)
        assertEquals(component.countOnStop, 0)
        component.exitStage(TestLifecycle.STAGE_ACTIVATED)
        assertEquals(component.countOnCreate, 1)
        assertEquals(component.countOnStart, 1)
        assertEquals(component.countOnStop, 1)

        component.enterStage(TestLifecycle.STAGE_ACTIVATED)
        assertEquals(component.countOnCreate, 1)
        assertEquals(component.countOnStart, 2)
        assertEquals(component.countOnStop, 1)
        component.exitStage(TestLifecycle.STAGE_ACTIVATED)
        assertEquals(component.countOnCreate, 1)
        assertEquals(component.countOnStart, 2)
        assertEquals(component.countOnStop, 2)

        component.enterStage(TestLifecycle.STAGE_ACTIVATED)
        assertEquals(component.countOnCreate, 1)
        assertEquals(component.countOnStart, 3)
        assertEquals(component.countOnStop, 2)
        component.exitStage(TestLifecycle.STAGE_ACTIVATED)
        assertEquals(component.countOnCreate, 1)
        assertEquals(component.countOnStart, 3)
        assertEquals(component.countOnStop, 3)

        component.enterStage(TestLifecycle.STAGE_ACTIVATED)
        assertEquals(component.countOnCreate, 1)
        assertEquals(component.countOnStart, 4)
        assertEquals(component.countOnStop, 3)
        component.exitStage(TestLifecycle.STAGE_ACTIVATED)
        assertEquals(component.countOnCreate, 1)
        assertEquals(component.countOnStart, 4)
        assertEquals(component.countOnStop, 4)

        // doOnStart was called from doOnCreate so after doOnDetach and reCreate it resubscribes
        component.exitStage(TestLifecycle.STAGE_CREATED)
        component.enterStage(TestLifecycle.STAGE_CREATED)
        assertEquals(component.countOnCreate, 2)

        component.enterStage(TestLifecycle.STAGE_ACTIVATED)
        assertEquals(component.countOnCreate, 2)
        assertEquals(component.countOnStart, 5)
        assertEquals(component.countOnStop, 4)
        component.exitStage(TestLifecycle.STAGE_ACTIVATED)
        assertEquals(component.countOnCreate, 2)
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
        component.enterStage(TestLifecycle.STAGE_CREATED)

        // start 1
        component.enterStage(TestLifecycle.STAGE_ACTIVATED)
        assertEquals(component.started, 1)
        component.exitStage(TestLifecycle.STAGE_ACTIVATED)
        assertEquals(component.started, 0)

        // start 2
        component.enterStage(TestLifecycle.STAGE_ACTIVATED)
        assertEquals(component.started, 1)
        component.exitStage(TestLifecycle.STAGE_ACTIVATED)
        assertEquals(component.started, 0)

        // now reenter parent stage
        component.exitStage(TestLifecycle.STAGE_CREATED)
        component.enterStage(TestLifecycle.STAGE_CREATED)

        // start 3
        component.enterStage(TestLifecycle.STAGE_ACTIVATED)
        assertEquals(component.started, 1)
        component.exitStage(TestLifecycle.STAGE_ACTIVATED)
        assertEquals(component.started, 0)

        // start 4
        component.enterStage(TestLifecycle.STAGE_ACTIVATED)
        assertEquals(component.started, 1)
        component.exitStage(TestLifecycle.STAGE_ACTIVATED)
        assertEquals(component.started, 0)

    }

}