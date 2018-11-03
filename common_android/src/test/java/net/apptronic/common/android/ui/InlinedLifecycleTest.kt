package net.apptronic.common.android.ui

import net.apptronic.common.android.ui.components.fragment.FragmentLifecycle
import net.apptronic.common.android.ui.components.fragment.FragmentViewModel
import net.apptronic.common.android.ui.threading.SynchronousExecutor
import net.apptronic.common.android.ui.threading.ThreadExecutor
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleHolder
import org.junit.Test
import kotlin.test.assertEquals

class InlinedLifecycleTest : LifecycleHolder<FragmentLifecycle> {

    private val lifecycle = FragmentLifecycle()

    override fun localLifecycle(): FragmentLifecycle = lifecycle

    override fun threadExecutor(): ThreadExecutor = SynchronousExecutor()

    private class SampleViewModel(lifecycleHolder: LifecycleHolder<FragmentLifecycle>) : FragmentViewModel(lifecycleHolder) {

        var countOnStart = 0
        var countOnStop = 0

    }

    @Test
    fun shouldCallSomeTimes() {
        val model = SampleViewModel(this).apply {
            onCreate {
                onStart {
                    countOnStart++
                    onExit {
                        countOnStop++
                    }
                }
            }
        }
        lifecycle.createdStage.enter()
        lifecycle.startedStage.enter()
        assertEquals(model.countOnStart, 1)
        assertEquals(model.countOnStop, 0)
        lifecycle.startedStage.exit()
        assertEquals(model.countOnStart, 1)
        assertEquals(model.countOnStop, 1)

        lifecycle.startedStage.enter()
        assertEquals(model.countOnStart, 2)
        assertEquals(model.countOnStop, 1)
        lifecycle.startedStage.exit()
        assertEquals(model.countOnStart, 2)
        assertEquals(model.countOnStop, 2)

        lifecycle.startedStage.enter()
        assertEquals(model.countOnStart, 3)
        assertEquals(model.countOnStop, 2)
        lifecycle.startedStage.exit()
        assertEquals(model.countOnStart, 3)
        assertEquals(model.countOnStop, 3)

        lifecycle.startedStage.enter()
        assertEquals(model.countOnStart, 4)
        assertEquals(model.countOnStop, 3)
        lifecycle.startedStage.exit()
        assertEquals(model.countOnStart, 4)
        assertEquals(model.countOnStop, 4)

        // onStart was called from onCreate so after onDestroy and reCreate it resubscribes
        lifecycle.createdStage.exit()
        lifecycle.createdStage.enter()

        lifecycle.startedStage.enter()
        assertEquals(model.countOnStart, 5)
        assertEquals(model.countOnStop, 4)
        lifecycle.startedStage.exit()
        assertEquals(model.countOnStart, 5)
        assertEquals(model.countOnStop, 5)

    }

}