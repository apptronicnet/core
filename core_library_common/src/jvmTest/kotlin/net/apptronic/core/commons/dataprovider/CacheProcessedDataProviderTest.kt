package net.apptronic.core.commons.dataprovider

import net.apptronic.core.BaseContextTest
import net.apptronic.core.base.subject.ValueHolder
import net.apptronic.core.base.subject.asValueHolder
import net.apptronic.core.commons.dataprovider.cache.DataProviderCacheProcessor
import net.apptronic.core.commons.dataprovider.cache.SimpleDataProviderCache
import net.apptronic.core.context.Context
import net.apptronic.core.context.childContext
import net.apptronic.core.context.dependencyModule
import net.apptronic.core.context.terminate
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.commons.genericEvent
import net.apptronic.core.entity.function.map
import org.junit.Test
import kotlin.test.assertEquals

class CacheProcessedDataProviderTest : BaseContextTest() {

    private val cache = SimpleDataProviderCache<Int, String>(100) { 1 }
    private val descriptor = dataProviderDescriptor<Int, String>()
    private val processor = object : DataProviderCacheProcessor<Int, String> {
        override fun processSet(key: Int, value: String): ValueHolder<String>? {
            return "$value-set".asValueHolder()
        }

        override fun processGet(key: Int, value: String): ValueHolder<String>? {
            return "$value-get".asValueHolder()
        }
    }

    private val readyEvent = genericEvent()

    inner class DataProviderImpl(context: Context, key: Int) : DataProvider<Int, String>(context, key) {

        override val dataProviderEntity: Entity<String> = readyEvent.switchContext(context).map {
            key.toString()
        }

    }

    init {
        context.dependencyModule {
            dataProviderCacheProcessor(descriptor) {
                processor
            }
            dataProviderCache(descriptor) {
                cache
            }
            sharedDataProvider(descriptor) {
                DataProviderImpl(scopedContext(), it)
            }
        }
    }

    @Test
    fun verifyProvidedDataIsProcessed() {
        childContext().apply {
            val data = injectData(descriptor, 1)
            readyEvent.update()
            assertEquals("1", data.get())
            assertEquals("1-set", cache[1]!!.value)
            terminate()
        }
        childContext().apply {
            val data = injectData(descriptor, 1)
            assertEquals("1-set-get", data.get())
            readyEvent.update()
            assertEquals("1", data.get())
            terminate()
        }
    }

}