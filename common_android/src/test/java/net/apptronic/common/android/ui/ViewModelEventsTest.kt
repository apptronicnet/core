package net.apptronic.common.android.ui

import net.apptronic.common.android.ui.threading.SynchronousExecutor
import net.apptronic.common.android.ui.threading.ThreadExecutor
import net.apptronic.common.android.ui.viewmodel.ViewModel
import net.apptronic.common.android.ui.viewmodel.lifecycle.GenericLifecycle
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleHolder
import org.junit.Test

class ViewModelEventsTest : LifecycleHolder<GenericLifecycle> {

    private val lifecycle = GenericLifecycle()

    override fun localLifecycle(): GenericLifecycle = lifecycle

    override fun threadExecutor(): ThreadExecutor = SynchronousExecutor()

    private class SampleViewModel(lifecycleHolder: LifecycleHolder<*>) : ViewModel(lifecycleHolder) {

        val genericEvent = genericEvent()

        val typedEvent = typedEvent<Int>()

    }

    @Test
    fun shouldAutoUnsubscribeOnExit() {
        val model = SampleViewModel(this)

        var genericCalls = 0
        var typedCalls = 0

        lifecycle.stage1.enter()
        model.genericEvent.subscribe {
            genericCalls++
        }
        model.typedEvent.subscribe {
            typedCalls++
        }
        model.genericEvent.sendEvent()
        model.typedEvent.sendEvent(74)

        assert(genericCalls == 1)
        assert(typedCalls == 1)

        lifecycle.stage1.exit()
        model.genericEvent.sendEvent()
        model.typedEvent.sendEvent(74)
        assert(genericCalls == 1)
        assert(typedCalls == 1)

        lifecycle.stage1.enter()
        model.genericEvent.sendEvent()
        model.typedEvent.sendEvent(74)
        assert(genericCalls == 1)
        assert(typedCalls == 1)

    }

    @Test
    fun shouldSendEvensAfterLifecycle() {
        val model = SampleViewModel(this)

        var genericCalls = 0
        var typedCalls = 0

        lifecycle.stage1.enter()

        model.genericEvent.subscribe {
            genericCalls++
        }
        model.typedEvent.subscribe {
            typedCalls++
            if (typedCalls == 1) {
                assert(it == 74)
            }
        }
        model.genericEvent.sendEvent()
        model.typedEvent.sendEvent(74)

        assert(genericCalls == 1)
        assert(typedCalls == 1)

    }

    @Test
    fun shouldNotSendEvensAfterLifecycleExit() {
        val model = SampleViewModel(this)

        var genericCalls = 0
        var typedCalls = 0

        lifecycle.stage1.enter()

        model.genericEvent.subscribe {
            genericCalls++
        }
        model.typedEvent.subscribe {
            typedCalls++
        }

        lifecycle.stage1.exit()

        model.genericEvent.sendEvent()
        model.typedEvent.sendEvent(74)

        assert(genericCalls == 0)
        assert(typedCalls == 0)

    }

}