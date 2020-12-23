package net.apptronic.core.commons.dataprovider.cache

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import net.apptronic.core.BaseContextTest
import net.apptronic.core.base.subject.asValueHolder
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SimpleCacheTest : BaseContextTest() {

    private val cache = SimpleDataProviderCache<Int, String>(maxSize = 3, sizeFunction = { 1 })

    @Test
    fun nullWhenNoCache() {
        assertNull(cache[1])
    }

    @Test
    fun simpleDataInCache() {
        cache[1] = "One"
        cache[2] = "Two"
        cache[3] = "Three"
        assertEquals("One".asValueHolder(), cache[1])
        assertEquals("Two".asValueHolder(), cache[2])
        assertEquals("Three".asValueHolder(), cache[3])
    }

    @Test
    fun dropsFromFirstUsed() {
        runBlocking {
            cache[1] = "One"
            delay(2)
            cache[2] = "Two"
            delay(2)
            cache[3] = "Three"
            delay(2)
            cache[1]
            delay(2)
            cache[2]
            delay(2)
            cache[4] = "Four"
            delay(2)
            assertEquals("One".asValueHolder(), cache[1])
            assertEquals("Two".asValueHolder(), cache[2])
            assertEquals("Four".asValueHolder(), cache[4])
            assertNull(cache[3])
        }
    }

    @Test
    fun releaseKeyWorks() {
        runBlocking {
            cache[1] = "One"
            delay(3)
            cache[2] = "Two"
            cache[3] = "Three"
            cache.releaseKey(3)
            cache[4] = "Four"
            assertEquals("One".asValueHolder(), cache[1])
            assertEquals("Two".asValueHolder(), cache[2])
            assertEquals("Four".asValueHolder(), cache[4])
            assertNull(cache[3])
        }
    }

}