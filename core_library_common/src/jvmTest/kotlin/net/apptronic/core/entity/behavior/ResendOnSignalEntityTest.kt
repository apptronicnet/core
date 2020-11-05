package net.apptronic.core.entity.behavior

import net.apptronic.core.entity.genericEvent
import net.apptronic.core.entity.typedEvent
import net.apptronic.core.entity.value
import net.apptronic.core.record
import net.apptronic.core.testutils.createTestContext
import org.junit.Test

class ResendOnSignalEntityTest {

    private val context = createTestContext()

    private val resendable = context.typedEvent<Int>()
    private val value = context.value<String>()
    private val event = context.genericEvent()

    @Test
    fun verifyResendEntity() {
        val results = resendable.resendWhen(value, event).record()
        results.assertItems()

        resendable.sendEvent(1)
        results.assertItems(1)

        value.set("1")
        results.assertItems(1, 1)

        event.sendEvent()
        results.assertItems(1, 1, 1)

        resendable.sendEvent(2)
        results.assertItems(1, 1, 1, 2)

        event.sendEvent()
        results.assertItems(1, 1, 1, 2, 2)

        value.set("1")
        results.assertItems(1, 1, 1, 2, 2)

        value.set("2")
        results.assertItems(1, 1, 1, 2, 2, 2)
    }

}