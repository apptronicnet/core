package net.apptronic.core.commons.dataprovider.cache

import net.apptronic.core.base.subject.ValueHolder
import net.apptronic.core.base.subject.asValueHolder
import org.junit.Test
import kotlin.test.assertEquals

class ProcessedCacheTest {

    private val processor = object : DataProviderCacheProcessor<Int, String> {
        override fun processSet(key: Int, value: String): ValueHolder<String>? {
            return "$value-set".asValueHolder()
        }

        override fun processGet(key: Int, value: String): ValueHolder<String>? {
            return "$value-get".asValueHolder()
        }
    }

    private val cache = ProcessedDataProviderCache(SimpleDataProviderCache<Int, String>(100) { 1 }, processor)

    @Test
    fun verifyProcessing() {
        cache[1] = "One"
        assertEquals(cache[1]!!.value, "One-set-get")

        cache[2] = "Two"
        lateinit var two: String
        cache.getAsync(2) {
            two = it
        }
        assertEquals(two, "Two-set-get")
    }

}