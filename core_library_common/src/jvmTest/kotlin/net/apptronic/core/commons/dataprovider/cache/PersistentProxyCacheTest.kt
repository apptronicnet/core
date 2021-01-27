package net.apptronic.core.commons.dataprovider.cache

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import net.apptronic.core.BaseContextTest
import net.apptronic.core.base.subject.ValueHolder
import net.apptronic.core.base.subject.asValueHolder
import net.apptronic.core.context.childContext
import net.apptronic.core.context.terminate
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class PersistentProxyCacheTest : BaseContextTest() {

    private suspend fun DataProviderCache<Int, String>.getAsync(key: Int): String {
        val deferred = CompletableDeferred<String>()
        getAsync(key) {
            deferred.complete(it)
        }
        return deferred.await()
    }

    val persistence = mutableMapOf<Int, String>()

    inner class PersistenceImpl : DataProviderCachePersistence<Int, String> {

        override suspend fun load(key: Int): ValueHolder<String>? {
            delay(3)
            return persistence[key]?.asValueHolder()
        }

        override suspend fun save(key: Int, value: String) {
            delay(3)
            persistence[key] = value
        }

        override fun clear() {
            persistence.clear()
        }

    }

    private fun newCacheInstance() = PersistentProxyDataProviderCache<Int, String>(
        context.childContext(),
        SimpleDataProviderCache(3, { 1 }),
        PersistenceImpl()
    )

    @Test
    fun verifyPersistentCache() {
        val cache1 = newCacheInstance()
        cache1[1] = "One"
        cache1[2] = "Two"
        cache1[3] = "Three"
        cache1[4] = "Four"
        cache1[5] = "Five"
        assertEquals(3, listOfNotNull(cache1[1], cache1[2], cache1[3], cache1[4], cache1[5]).size)
        runBlocking {
            delay(10)
            assertEquals(
                5,
                listOfNotNull(
                    cache1.getAsync(1),
                    cache1.getAsync(2),
                    cache1.getAsync(3),
                    cache1.getAsync(4),
                    cache1.getAsync(5)
                ).size
            )
        }
        cache1.terminate()

        val cache2 = newCacheInstance()
        assertEquals(0, listOfNotNull(cache2[1], cache2[2], cache2[3], cache2[4], cache2[5]).size)
        runBlocking {
            assertEquals(
                5,
                listOfNotNull(
                    cache2.getAsync(1),
                    cache2.getAsync(2),
                    cache2.getAsync(3),
                    cache2.getAsync(4),
                    cache2.getAsync(5)
                ).size
            )
            assertEquals(3, listOfNotNull(cache2[1], cache2[2], cache2[3], cache2[4], cache2[5]).size)
        }
    }

    @Test
    fun cacheClears() {
        val cache = newCacheInstance()
        cache[1] = "One"
        cache[2] = "Two"
        cache[3] = "Three"
        runBlocking {
            delay(10)

            cache.clear()

            assertNull(cache[1])
            assertNull(cache[2])
            assertNull(cache[3])

            // persistence is cleared only by separate request
            assertNotNull(persistence[1])
            assertNotNull(persistence[2])
            assertNotNull(persistence[3])
        }
    }


}