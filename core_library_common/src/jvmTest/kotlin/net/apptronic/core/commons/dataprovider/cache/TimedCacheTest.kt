package net.apptronic.core.commons.dataprovider.cache

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import net.apptronic.core.BaseContextTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class TimedCacheTest : BaseContextTest() {

    private val cache = TimedDataProviderCache<Int, String>(context, maxSize = 3, expirationTime = 10L)

    @Test
    fun shouldCleanUpAfterExpiration() {
        runBlocking {
            cache[1] = "1"
            cache[2] = "2"
            cache[3] = "3"
            assertNotNull(cache[1])
            assertNotNull(cache[2])
            assertNotNull(cache[3])
            cache.releaseKey(1)
            cache.releaseKey(2)
            delay(20)
            assertNull(cache[1])
            assertNull(cache[2])
            assertNotNull(cache[3])
        }
    }

    @Test
    fun shouldCancelExpirationWhenUsed() {
        runBlocking {
            cache[1] = "1"
            assertNotNull(cache[1])
            cache.releaseKey(1)
            assertNotNull(cache[1])
            delay(20)
            assertNotNull(cache[1])
        }
    }

    @Test
    fun shouldTrim() {
        runBlocking {
            cache[1] = "1"
            delay(3)
            cache[2] = "2"
            delay(3)
            cache[3] = "3"
            delay(3)
            cache[4] = "4"
            val data = listOf(cache[1], cache[2], cache[3], cache[4]).filterNotNull()
            assertEquals(3, data.size)
        }
    }

}