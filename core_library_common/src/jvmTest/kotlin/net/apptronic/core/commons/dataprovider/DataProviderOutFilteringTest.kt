package net.apptronic.core.commons.dataprovider

import net.apptronic.core.BaseContextTest
import net.apptronic.core.context.Context
import net.apptronic.core.context.dependencyModule
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.commons.value
import net.apptronic.core.entity.function.map
import kotlin.test.Test
import kotlin.test.assertEquals

class DataProviderOutFilteringTest : BaseContextTest() {

    val descriptor = dataProviderDescriptor<Int, String>()

    class DataProviderImpl(context: Context, key: Int) : DataProvider<Int, String>(context, key) {

        override val outData: Entity<String> = data.map { "$it-suffix" }

        override val dataProviderEntity: Entity<String> = value(key.toString())

    }

    init {
        context.dependencyModule {
            sharedDataProvider(descriptor) {
                DataProviderImpl(scopedContext(), it)
            }
        }
    }

    @Test
    fun verifyOutFiltering() {
        val data = injectData(descriptor, 1)
        assertEquals("1-suffix", data.get())
    }

}