package net.apptronic.core.component.entity.behavior

import net.apptronic.core.component.genericEvent
import net.apptronic.core.component.typedEvent
import net.apptronic.core.component.value
import net.apptronic.core.record
import net.apptronic.core.testutils.testContext
import org.junit.Test

class CombineSignalEntityTest {

    val context = testContext()

    @Test
    fun verifyCombineSignalEntity() {
        val source1 = context.value<String>()
        val source2 = context.value<Int>()
        val source3 = context.typedEvent<Boolean>()
        val source4 = context.genericEvent()

        source1.set("Str")
        source2.set(0)

        val result = context.whenAnyUpdates(source1, source2, source3, source4).record()

        result.assertSize(0)

        source1.set("Str2")
        source2.set(1)
        result.assertSize(2)

        source1.set("Some")
        source2.set(2)
        source2.set(3)
        result.assertSize(5)

        source3.sendEvent(true)
        source3.sendEvent(false)
        result.assertSize(7)

        source4.sendEvent()
        result.assertSize(8)

        source4.sendEvent()
        result.assertSize(9)

        source1.set("Another")
        source2.set(123)
        source3.sendEvent(true)
        source4.sendEvent()
        result.assertSize(13)
    }

}