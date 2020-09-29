package net.apptronic.core.component.lifecycle

import net.apptronic.core.component.genericEvent
import net.apptronic.core.component.typedEvent
import net.apptronic.core.testutils.BaseTestComponent
import net.apptronic.core.testutils.TestLifecycle
import org.junit.Test

class LifecycleEventsTest {

    private class TestComponent : BaseTestComponent() {

        val genericEvent = genericEvent()

        val typedEvent = typedEvent<Int>()

    }

    private val component = TestComponent()
    private val lifecycle = component.getLifecycle()

    @Test
    fun shouldAutoUnsubscribeOnExit() {

        var genericCalls = 0
        var typedCalls = 0

        lifecycle.enterStage(TestLifecycle.STAGE_CREATED)
        component.genericEvent.subscribe {
            genericCalls++
        }
        component.typedEvent.subscribe {
            typedCalls++
        }
        component.genericEvent.sendEvent()
        component.typedEvent.sendEvent(74)

        assert(genericCalls == 1)
        assert(typedCalls == 1)

        lifecycle.exitStage(TestLifecycle.STAGE_CREATED)
        component.genericEvent.sendEvent()
        component.typedEvent.sendEvent(74)
        assert(genericCalls == 1)
        assert(typedCalls == 1)

        lifecycle.enterStage(TestLifecycle.STAGE_CREATED)
        component.genericEvent.sendEvent()
        component.typedEvent.sendEvent(74)
        assert(genericCalls == 1)
        assert(typedCalls == 1)

    }

    @Test
    fun shouldSendEvensAfterLifecycle() {
        var genericCalls = 0
        var typedCalls = 0

        lifecycle.enterStage(TestLifecycle.STAGE_CREATED)

        component.genericEvent.subscribe {
            genericCalls++
        }
        component.typedEvent.subscribe {
            typedCalls++
            if (typedCalls == 1) {
                assert(it == 74)
            }
        }
        component.genericEvent.sendEvent()
        component.typedEvent.sendEvent(74)

        assert(genericCalls == 1)
        assert(typedCalls == 1)

    }

    @Test
    fun shouldNotSendEvensAfterLifecycleExit() {
        var genericCalls = 0
        var typedCalls = 0

        lifecycle.enterStage(TestLifecycle.STAGE_CREATED)

        component.genericEvent.subscribe {
            genericCalls++
        }
        component.typedEvent.subscribe {
            typedCalls++
        }

        lifecycle.exitStage(TestLifecycle.STAGE_CREATED)

        component.genericEvent.sendEvent()
        component.typedEvent.sendEvent(74)

        assert(genericCalls == 0)
        assert(typedCalls == 0)

    }

    @Test
    fun shouldSendOnceEventBeforeEnter() {
        var oneCalled = 0
        var oneAltCalled = 0
        var twoCalled = 0

        component.doOnceActivated("one") { oneCalled++ }

        component.doOnceActivated("two") { twoCalled++ }

        lifecycle.enterStage(TestLifecycle.STAGE_CREATED)
        lifecycle.enterStage(TestLifecycle.STAGE_ACTIVATED)

        // each called once
        assert(oneCalled == 1)
        assert(oneAltCalled == 0)
        assert(twoCalled == 1)

        // re-enter
        lifecycle.exitStage(TestLifecycle.STAGE_ACTIVATED)
        lifecycle.enterStage(TestLifecycle.STAGE_ACTIVATED)

        // no more calls
        assert(oneCalled == 1)
        assert(oneAltCalled == 0)
        assert(twoCalled == 1)

        lifecycle.exitStage(TestLifecycle.STAGE_ACTIVATED)

        // add callback
        component.doOnceActivated("one") { oneCalled++ }
        // replace by key
        component.doOnceActivated("one") { oneAltCalled++ }
        lifecycle.enterStage(TestLifecycle.STAGE_ACTIVATED)

        // called alt version
        assert(oneCalled == 1)
        assert(oneAltCalled == 1)
        assert(twoCalled == 1)

        lifecycle.exitStage(TestLifecycle.STAGE_ACTIVATED)

        // new shot
        component.doOnceActivated("one") { oneCalled++ }
        // exit and reenter parent stage
        lifecycle.exitStage(TestLifecycle.STAGE_CREATED)
        lifecycle.enterStage(TestLifecycle.STAGE_CREATED)
        lifecycle.enterStage(TestLifecycle.STAGE_ACTIVATED)

        // still should be called after exit from parent stages
        assert(oneCalled == 2)

        // subscribe when already entered
        component.doOnceActivated("one") { oneCalled++ }
        // should be invoked immediately
        assert(oneCalled == 3)

    }

}