package net.apptronic.core.commons.dataprovider

import kotlinx.coroutines.CompletableDeferred
import net.apptronic.core.BaseContextTest
import net.apptronic.core.base.subject.ValueHolder
import net.apptronic.core.base.subject.asValueHolder
import net.apptronic.core.commons.dataprovider.cache.DataProviderCache
import net.apptronic.core.commons.dataprovider.cache.DataProviderCachePersistence
import net.apptronic.core.commons.dataprovider.cache.SimpleDataProviderCache
import net.apptronic.core.commons.eventbus.eventBus
import net.apptronic.core.commons.eventbus.eventBusClient
import net.apptronic.core.context.Context
import net.apptronic.core.context.childContext
import net.apptronic.core.context.dependencyModule
import net.apptronic.core.context.terminate
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.commons.unitEntity
import net.apptronic.core.entity.function.mapSuspend
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

object LoadData

class DataProviderDispatcherDropDataTest : BaseContextTest() {

    val DataDescriptor = dataProviderDescriptor<Int, String>()

    var initializedCount = 0
    var terminatedCount = 0
    var clearCalledCount = 0
    var clearPersistenceCalledCount = 0

    private val simpleCache = SimpleDataProviderCache<Int, String>(999, { 1 })

    private val cache = object : DataProviderCache<Int, String> by simpleCache {
        override fun clear() {
            clearCalledCount++
            simpleCache.clear()
        }
    }

    private val persistence = object : DataProviderCachePersistence<Int, String> {

        val persistence = mutableMapOf<Int, String>()

        override suspend fun load(key: Int): ValueHolder<String>? {
            return persistence[key]?.asValueHolder()
        }

        override suspend fun save(key: Int, value: String) {
            persistence[key] = value
        }

        override fun clear() {
            super.clear()
            clearPersistenceCalledCount++
            persistence.clear()
        }

    }

    fun assertCounters(initialized: Int, terminated: Int) {
        assertEquals(initializedCount, initialized)
        assertEquals(terminatedCount, terminated)
    }

    inner class DataProviderImpl(context: Context, key: Int) : DataProvider<Int, String>(context, key) {

        val bus = eventBusClient()
        val deferred = CompletableDeferred<Unit>()

        override val dataProviderEntity: Entity<String> = unitEntity().mapSuspend {
            deferred.await()
            key.toString()
        }

        init {
            initializedCount++
            doOnTerminate {
                terminatedCount++
            }
            bus.subscribe {
                if (it == LoadData) {
                    deferred.complete(Unit)
                }
            }
        }

    }

    init {
        context.dependencyModule {
            eventBus()
            dataProviderCachePersistence(DataDescriptor) {
                persistence
            }
            dataProviderCache(DataDescriptor) {
                cache
            }
            sharedDataProvider(DataDescriptor, fallbackCount = 999, fallbackLifetime = 999_999) { key ->
                DataProviderImpl(scopedContext(), key)
            }
        }
    }

    val dispatcher = injectDataDispatcher(DataDescriptor)

    var var1: String? = null
    var var2: String? = null
    var var3: String? = null

    val child1 = childContext().apply { injectData(DataDescriptor, 1).subscribe { var1 = it } }
    val child2 = childContext().apply { injectData(DataDescriptor, 2).subscribe { var2 = it } }
    val child3 = childContext().apply { injectData(DataDescriptor, 3).subscribe { var3 = it } }

    val bus = eventBusClient()

    @Test
    fun verifyNoValueAfterDrop() {
        bus.postEvent(LoadData)
        assertEquals("1", var1)
        assertEquals("2", var2)
        assertEquals("3", var3)

        dispatcher.resetData(DataAndCache(true))

        var var1_1: String? = null
        childContext().apply { injectData(DataDescriptor, 1).subscribe { var1_1 = it } }
        assertNull(var1_1)

        bus.postEvent(LoadData)
        assertEquals(var1_1, "1")
    }

    @Test
    fun verifyDropOnlyData() {
        assertCounters(3, 0)
        dispatcher.resetData(OnlyData)
        assertCounters(6, 3)
        assertEquals(0, clearCalledCount)
        assertEquals(0, clearPersistenceCalledCount)
    }

    @Test
    fun verifyDropCacheNoPersistence() {
        assertCounters(3, 0)
        dispatcher.resetData(DataAndCache(false))
        assertCounters(6, 3)
        assertEquals(1, clearCalledCount)
        assertEquals(0, clearPersistenceCalledCount)
    }

    @Test
    fun verifyDropCacheAndPersistence() {
        assertCounters(3, 0)
        dispatcher.resetData(DataAndCache(true))
        assertCounters(6, 3)
        assertEquals(1, clearCalledCount)
        assertEquals(1, clearPersistenceCalledCount)
    }

    @Test
    fun verifyFallbackNotRecreated() {
        assertCounters(3, 0)
        assertNull(var1)
        assertNull(var2)
        assertNull(var3)

        child1.terminate()
        child2.terminate()

        dispatcher.resetData(DataAndCache(true))
        assertCounters(4, 3)

        bus.postEvent(LoadData)

        assertNull(var1)
        assertNull(var2)
        assertEquals("3", var3)
    }

}