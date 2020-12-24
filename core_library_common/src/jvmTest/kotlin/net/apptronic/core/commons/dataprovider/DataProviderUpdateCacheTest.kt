package net.apptronic.core.commons.dataprovider

import net.apptronic.core.BaseContextTest
import net.apptronic.core.commons.dataprovider.cache.SimpleDataProviderCache
import net.apptronic.core.context.Context
import net.apptronic.core.context.dependencyModule
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.commons.value
import org.junit.Test
import kotlin.test.assertEquals

class DataProviderUpdateCacheTest : BaseContextTest() {

    private val Descriptor = dataProviderDescriptor<Int, String>()

    private val cache = SimpleDataProviderCache<Int, String>(100, { 1 })

    private class ProviderImpl(context: Context, key: Int) : DataProvider<Int, String>(context, key) {

        override val dataProviderEntity: Entity<String>
            get() = value(key.toString())

    }

    init {
        context.dependencyModule {
            dataProviderCache(Descriptor) {
                cache
            }
            sharedDataProvider(Descriptor) { key ->
                ProviderImpl(scopedContext(), key)
            }
        }
    }

    @Test
    fun verifyCacheWritten() {
        injectData(Descriptor, 1)
        assertEquals("1", cache[1]!!.value)

        injectData(Descriptor, 2)
        assertEquals("2", cache[2]!!.value)
    }

}