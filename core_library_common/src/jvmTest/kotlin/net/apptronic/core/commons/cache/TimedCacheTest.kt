package net.apptronic.core.commons.cache

import net.apptronic.core.BaseContextTest
import org.junit.Test
import kotlin.test.fail

class TimedCacheTest : BaseContextTest() {

    private val cache = TimedCache<Int, String>(context, maxFallbackCount = 3, fallbackLifetimeMillis = 10L)

    @Test
    fun notWrittenYet() {
        fail("Not written yet")
    }

}