package net.apptronic.core.entity.behavior

import kotlinx.coroutines.test.TestCoroutineDispatcher
import net.apptronic.core.context.coroutines.CoroutineDispatchers
import net.apptronic.core.entity.entities.asProperty
import net.apptronic.core.entity.value
import net.apptronic.core.testutils.createTestContext
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class DelayEntityTest {

    val testDispatcher = TestCoroutineDispatcher()
    val context = createTestContext(
            coroutineDispatchers = CoroutineDispatchers(testDispatcher)
    )

    @Test
    fun verifyDelayEntity() {
        val source = context.value<String>()
        val result = source.delay(256).asProperty()
        val startTime = testDispatcher.currentTime
        source.set("Value")
        testDispatcher.advanceTimeBy(255)
        assertFalse(result.isSet())
        testDispatcher.advanceTimeBy(1)
        assertEquals(result.get(), "Value")
        val endTime = testDispatcher.currentTime
        assertEquals(startTime + 256L, endTime)
    }

}