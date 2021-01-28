package net.apptronic.core.base.lang

import net.apptronic.core.BaseContextTest
import net.apptronic.core.entity.commons.genericEvent
import net.apptronic.core.entity.commons.value
import net.apptronic.core.entity.conditions.awaitAny
import net.apptronic.core.entity.function.mapSuspend
import org.junit.Test
import kotlin.test.assertEquals

class AttemptTest : BaseContextTest() {

    val retry = genericEvent()

    val source = value<Int>()

    var throwException = false

    var exceptionsCount = 0

    val result = source.mapSuspend {
        attempt {
            if (throwException) {
                throw RuntimeException("Failed to try")
            }
            it.toString()
        }.retryWhen {
            exceptionsCount++
            retry.awaitAny()
        }
    }

    @Test
    fun verifyTryDoRetryWhen() {

        source.set(1)
        assertEquals("1", result.get())
        assertEquals(0, exceptionsCount)

        throwException = true

        source.set(2)
        assertEquals("1", result.get())
        assertEquals(1, exceptionsCount)

        retry.update()
        assertEquals("1", result.get())
        assertEquals(2, exceptionsCount)

        throwException = false
        retry.update()

        assertEquals("2", result.get())
        assertEquals(2, exceptionsCount)
    }

}