package net.apptronic.core.commons.dataprovider.cache

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import net.apptronic.core.BaseContextTest
import net.apptronic.core.base.subject.ValueHolder
import net.apptronic.core.base.subject.asValueHolder
import net.apptronic.core.context.childContext
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class PersistentCacheTest : BaseContextTest() {

    private val persistence = mutableMapOf<Int, String>()

    private suspend fun DataProviderCache<Int, String>.getAsync(key: Int): String {
        val deferred = CompletableDeferred<String>()
        getAsync(key) {
            deferred.complete(it)
        }
        return deferred.await()
    }

    private inner class PersistenceImpl : CachePersistence<Int, String> {

        override suspend fun load(key: Int): ValueHolder<String>? {
            delay(3)
            return persistence[key]?.asValueHolder()
        }

        override suspend fun save(key: Int, value: String) {
            delay(3)
            persistence[key] = value
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

}