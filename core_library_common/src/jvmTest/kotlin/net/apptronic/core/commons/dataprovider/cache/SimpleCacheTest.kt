package net.apptronic.core.commons.dataprovider.cache

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import net.apptronic.core.BaseContextTest
import net.apptronic.core.base.subject.asValueHolder
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class SimpleCacheTest : BaseContextTest() {


    @Test
    fun nullWhenNoCache() {
        val cache = SimpleDataProviderCache<Int, String>(maxSize = 3, sizeFunction = { 1 })
        assertNull(cache[1])
    }

    @Test
    fun simpleDataInCache() {
        val cache = SimpleDataProviderCache<Int, String>(maxSize = 3, sizeFunction = { 1 })
        cache[1] = "One"
        cache[2] = "Two"
        cache[3] = "Three"
        assertEquals("One".asValueHolder(), cache[1])
        assertEquals("Two".asValueHolder(), cache[2])
        assertEquals("Three".asValueHolder(), cache[3])
    }

    @Test
    fun dropsWhenExceedLimit() {
        val cache = SimpleDataProviderCache<Int, String>(maxSize = 3, sizeFunction = { 1 })
        cache[1] = "One"
        cache[2] = "Two"
        cache[3] = "Three"
        cache[4] = "Four"
        val cached = listOfNotNull(cache[1], cache[2], cache[3], cache[4])
        assertEquals(3, cached.size)
    }

    @Test
    fun releaseKeyWorks() {
        val cache = SimpleDataProviderCache<Int, String>(maxSize = 3, sizeFunction = { 1 })
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

    @Test
    fun sizeFunctionWorks() {
        val cache = SimpleDataProviderCache<Int, String>(maxSize = 10, sizeFunction = { it.second.length })
        cache[1] = "One" // 3
        cache[2] = "Two" // +3 = 6
        cache[3] = "Three" // +5 = 11 > 10 (limit)
        val cached = listOfNotNull(cache[1], cache[2], cache[3])
        assertEquals(2, cached.size)
    }

    @Test
    fun cacheClears() {
        val cache = SimpleDataProviderCache<Int, String>(maxSize = 10, sizeFunction = { 1 })
        cache[1] = "One"
        cache[2] = "Two"
        cache[3] = "Three"

        assertNotNull(cache[1])
        assertNotNull(cache[2])
        assertNotNull(cache[3])

        cache.clear()

        assertNull(cache[1])
        assertNull(cache[2])
        assertNull(cache[3])
    }

}