package net.apptronic.core.component.entity.behavior

import net.apptronic.core.component.typedEvent
import net.apptronic.core.testutils.testContext
import org.junit.Test
import kotlin.test.assertEquals

class ActionExtensionsTest {

    val context = testContext()
    val source = context.typedEvent<Boolean>()
    var callsWhen = 0
    var callsElse = 0

    @Test
    fun verifyWhenElse() {
        context.doWhen(source) {
            callsWhen++
        } orElseDo {
            callsElse++
        }

        source.sendEvent(true)
        assertEquals(callsWhen, 1)
        assertEquals(callsElse, 0)

        source.sendEvent(true)
        assertEquals(callsWhen, 2)
        assertEquals(callsElse, 0)

        source.sendEvent(false)
        assertEquals(callsWhen, 2)
        assertEquals(callsElse, 1)

        source.sendEvent(false)
        assertEquals(callsWhen, 2)
        assertEquals(callsElse, 2)

        source.sendEvent(false)
        source.sendEvent(false)
        source.sendEvent(false)
        source.sendEvent(false)
        assertEquals(callsWhen, 2)
        assertEquals(callsElse, 6)

        source.sendEvent(true)
        assertEquals(callsWhen, 3)
        assertEquals(callsElse, 6)
    }

    @Test
    fun verifyWhenNot() {
        context.doWhenNot(source) {
            callsWhen++
        } orElseDo {
            callsElse++
        }

        source.sendEvent(true)
        assertEquals(callsWhen, 0)
        assertEquals(callsElse, 1)

        source.sendEvent(false)
        source.sendEvent(false)
        assertEquals(callsWhen, 2)
        assertEquals(callsElse, 1)
    }

    @Test
    fun verifyWhenElseSuspend() {
        context.doWhenSuspend(source) {
            callsWhen++
        } orElseDo {
            callsElse++
        }

        source.sendEvent(true)
        assertEquals(callsWhen, 1)
        assertEquals(callsElse, 0)

        source.sendEvent(true)
        assertEquals(callsWhen, 2)
        assertEquals(callsElse, 0)

        source.sendEvent(false)
        assertEquals(callsWhen, 2)
        assertEquals(callsElse, 1)

        source.sendEvent(false)
        assertEquals(callsWhen, 2)
        assertEquals(callsElse, 2)

        source.sendEvent(false)
        source.sendEvent(false)
        source.sendEvent(false)
        source.sendEvent(false)
        assertEquals(callsWhen, 2)
        assertEquals(callsElse, 6)

        source.sendEvent(true)
        assertEquals(callsWhen, 3)
        assertEquals(callsElse, 6)
    }

    @Test
    fun verifyWhenNotSuspend() {
        context.doWhenNotSuspend(source) {
            callsWhen++
        } orElseDo {
            callsElse++
        }

        source.sendEvent(true)
        assertEquals(callsWhen, 0)
        assertEquals(callsElse, 1)

        source.sendEvent(false)
        source.sendEvent(false)
        assertEquals(callsWhen, 2)
        assertEquals(callsElse, 1)
    }

}