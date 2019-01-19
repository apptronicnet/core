package net.apptronic.common.android.ui

import net.apptronic.common.android.ui.components.fragment.FragmentLifecycle
import net.apptronic.common.android.ui.components.fragment.FragmentViewModel
import net.apptronic.common.android.ui.threading.SynchronousExecutor
import net.apptronic.common.android.ui.viewmodel.lifecycle.Lifecycle
import org.junit.Test
import kotlin.test.assertEquals

class InlinedLifecycleTest {

    private val lifecycle = FragmentLifecycle(SynchronousExecutor()).apply { start() }

    private class SampleViewModel(lifecycle: Lifecycle) : FragmentViewModel(lifecycle) {

        var countOnStart = 0
        var countOnStop = 0

        var started = 0

    }

    @Test
    fun shouldCallSomeTimes() {
        val model = SampleViewModel(lifecycle).apply {
            onCreate {
                onStart {
                    countOnStart++
                    onExit {
                        countOnStop++
                    }
                }
            }
        }
        lifecycle.getCreatedStage().enter()
        lifecycle.getStartedStage().enter()
        assertEquals(model.countOnStart, 1)
        assertEquals(model.countOnStop, 0)
        lifecycle.getStartedStage().exit()
        assertEquals(model.countOnStart, 1)
        assertEquals(model.countOnStop, 1)

        lifecycle.getStartedStage().enter()
        assertEquals(model.countOnStart, 2)
        assertEquals(model.countOnStop, 1)
        lifecycle.getStartedStage().exit()
        assertEquals(model.countOnStart, 2)
        assertEquals(model.countOnStop, 2)

        lifecycle.getStartedStage().enter()
        assertEquals(model.countOnStart, 3)
        assertEquals(model.countOnStop, 2)
        lifecycle.getStartedStage().exit()
        assertEquals(model.countOnStart, 3)
        assertEquals(model.countOnStop, 3)

        lifecycle.getStartedStage().enter()
        assertEquals(model.countOnStart, 4)
        assertEquals(model.countOnStop, 3)
        lifecycle.getStartedStage().exit()
        assertEquals(model.countOnStart, 4)
        assertEquals(model.countOnStop, 4)

        // onStart was called from onCreate so after onDestroy and reCreate it resubscribes
        lifecycle.getCreatedStage().exit()
        lifecycle.getCreatedStage().enter()

        lifecycle.getStartedStage().enter()
        assertEquals(model.countOnStart, 5)
        assertEquals(model.countOnStop, 4)
        lifecycle.getStartedStage().exit()
        assertEquals(model.countOnStart, 5)
        assertEquals(model.countOnStop, 5)

    }

    @Test
    fun shouldBeConsistent() {
        val model = SampleViewModel(lifecycle).apply {
            onCreate {
                onStart {
                    started++
                }
                onStop {
                    started--
                }
            }
        }

        // first cycle
        lifecycle.getCreatedStage().enter()

        // start 1
        lifecycle.getStartedStage().enter()
        assertEquals(model.started, 1)
        lifecycle.getStartedStage().exit()
        assertEquals(model.started, 0)

        // start 2
        lifecycle.getStartedStage().enter()
        assertEquals(model.started, 1)
        lifecycle.getStartedStage().exit()
        assertEquals(model.started, 0)

        // now reenter parent stage
        lifecycle.getCreatedStage().exit()
        lifecycle.getCreatedStage().enter()

        // start 3
        lifecycle.getStartedStage().enter()
        assertEquals(model.started, 1)
        lifecycle.getStartedStage().exit()
        assertEquals(model.started, 0)

        // start 4
        lifecycle.getStartedStage().enter()
        assertEquals(model.started, 1)
        lifecycle.getStartedStage().exit()
        assertEquals(model.started, 0)

    }

}