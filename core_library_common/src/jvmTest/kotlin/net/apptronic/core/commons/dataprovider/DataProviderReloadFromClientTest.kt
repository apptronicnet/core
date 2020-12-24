package net.apptronic.core.commons.dataprovider

import kotlinx.coroutines.runBlocking
import net.apptronic.core.BaseContextTest
import net.apptronic.core.commons.dataprovider.cache.SimpleDataProviderCache
import net.apptronic.core.context.Context
import net.apptronic.core.context.dependencyModule
import net.apptronic.core.entity.commons.value
import org.junit.Test
import kotlin.test.assertEquals

class DataProviderReloadFromClientTest : BaseContextTest() {

    private val Descriptor = dataProviderDescriptor<Int, Int>()

    private val cache = SimpleDataProviderCache<Int, Int>(100, { 1 })

    private class SerialDataProvider(context: Context, key: Int) : DataProvider<Int, Int>(context, key) {

        var value = 1

        override val dataProviderEntity = value(value++)

        override suspend fun loadData(): Int {
            return value++
        }

    }

    init {
        context.dependencyModule {
            dataProviderCache(Descriptor) {
                cache
            }
            sharedDataProvider(Descriptor) { key ->
                SerialDataProvider(scopedContext(), key)
            }
        }
    }

    @Test
    fun verifyLoadFromClientAndSaveToCache() {
        runBlocking {
            val data1 = injectData(Descriptor, 1)
            val data2 = injectData(Descriptor, 2)

            assertEquals(1, data1.get())
            assertEquals(1, data2.get())

            assertEquals(1, cache[1]!!.value)
            assertEquals(1, cache[2]!!.value)

            assertEquals(2, data1.reload())
            assertEquals(2, data1.get())
            assertEquals(2, cache[1]!!.value)

            data1.postReload()
            assertEquals(3, data1.get())
            assertEquals(3, cache[1]!!.value)
        }
    }

}