package net.apptronic.core.commons.dataprovider.cache

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import net.apptronic.core.BaseContextTest
import net.apptronic.core.base.subject.ValueHolder
import net.apptronic.core.base.subject.asValueHolder
import net.apptronic.core.context.childContext
import net.apptronic.core.entity.commons.genericEvent
import net.apptronic.core.entity.conditions.awaitAny
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class PersistentCacheTest : BaseContextTest() {

    private val persistence = mutableMapOf<Int, String>()
    private val clock = genericEvent()

    private suspend fun DataProviderCache<Int, String>.getAsync(key: Int): String {
        val deferred = CompletableDeferred<String>()
        getAsync(key) {
            deferred.complete(it)
        }
        return deferred.await()
    }

    private inner class PersistenceImpl : DataProviderCachePersistence<Int, String> {

        override suspend fun load(key: Int): ValueHolder<String>? {
            return persistence[key]?.asValueHolder()
        }

        override suspend fun save(key: Int, value: String) {
            clock.awaitAny()
            persistence[key] = value
        }

        override fun clear() {
            persistence.clear()
        }

    }

    private val cache = PersistentDataProviderCache<Int, String>(
        context.childContext(),
        PersistenceImpl()
    )

    @Test
    fun shouldSaveAndLoad() {
        runBlocking {
            cache[1] = "One"
            cache[2] = "Two"
            cache[3] = "Three"
            assertEquals("One", cache.getAsync(1))
            assertEquals("Two", cache.getAsync(2))
            assertEquals("Three", cache.getAsync(3))
            assertEquals("One", persistence[1]!!)
            assertEquals("Two", persistence[2]!!)
            assertEquals("Three", persistence[3]!!)
        }
    }

    @Test
    fun shouldReturnNullForGet() {
        cache[1] = "One"
        cache[2] = "Two"
        cache[3] = "Three"
        assertNull(cache[1])
        assertNull(cache[2])
        assertNull(cache[3])
    }

    @Test
    fun shouldIgnoreRelease() {
        runBlocking {
            cache[1] = "One"
            cache[2] = "Two"
            cache[3] = "Three"
            cache.releaseKey(1)
            cache.releaseKey(2)
            cache.releaseKey(3)

            assertEquals("One", cache.getAsync(1))
            assertEquals("Two", cache.getAsync(2))
            assertEquals("Three", cache.getAsync(3))
            assertEquals("One", persistence[1]!!)
            assertEquals("Two", persistence[2]!!)
            assertEquals("Three", persistence[3]!!)
        }
    }

    @Test
    fun cacheClears() {
        runBlocking {
            cache[1] = "One"
            cache[2] = "Two"
            cache[3] = "Three"

            clock.update()

            assertNotNull(persistence[1])
            assertNotNull(persistence[2])
            assertNotNull(persistence[3])

            cache.clear()

            assertNull(persistence[1])
            assertNull(persistence[2])
            assertNull(persistence[3])
        }
    }


}