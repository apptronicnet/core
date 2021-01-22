package net.apptronic.core.commons.dataprovider

import net.apptronic.core.BaseContextTest
import net.apptronic.core.context.Context
import net.apptronic.core.context.dependencyModule
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.commons.value
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class DataProviderDynamicKeyPropertyTest : BaseContextTest() {

    var activeProvidersCount = 0

    inner class DataProviderImpl(context: Context, key: Int) : DataProvider<Int, String>(context, key) {

        override val dataProviderEntity: Entity<String> = value(key.toString())

        init {
            activeProvidersCount++
            doOnTerminate {
                activeProvidersCount--
            }
        }

    }

    val descriptor = dataProviderDescriptor<Int, String>()

    init {
        context.dependencyModule {
            sharedDataProvider(descriptor) {
                DataProviderImpl(scopedContext(), it)
            }
        }
    }

    val key = value<Int>()
    val data = injectData(descriptor, key)

    @Test
    fun changingPropertyTest() {
        assertEquals(0, activeProvidersCount)
        assertFalse(data.isSet())

        key.set(1)
        assertEquals(1, activeProvidersCount)
        assertEquals("1", data.get())

        key.set(2)
        assertEquals(1, activeProvidersCount)
        assertEquals("2", data.get())
    }

}