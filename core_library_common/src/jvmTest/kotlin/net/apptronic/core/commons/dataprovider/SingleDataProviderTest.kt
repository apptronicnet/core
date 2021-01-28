package net.apptronic.core.commons.dataprovider

import net.apptronic.core.BaseContextTest
import net.apptronic.core.context.Context
import net.apptronic.core.context.childContext
import net.apptronic.core.context.dependencyModule
import net.apptronic.core.context.terminate
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.commons.value
import org.junit.Test
import kotlin.test.assertEquals

class SingleDataProviderTest : BaseContextTest() {

    val descriptor = dataProviderDescriptor<Unit, String>()

    var initializedCount = 0
    var terminatedCount = 0

    inner class DataProviderImpl(context: Context) : DataProvider<Unit, String>(context, Unit) {

        override val dataProviderEntity: Entity<String> = value("Data!")

        init {
            initializedCount++
            doOnTerminate {
                terminatedCount++
            }
        }

    }

    fun load(initOnLoad: Boolean) {
        context.dependencyModule {
            singleDataProvider(descriptor, initOnLoad = initOnLoad) {
                DataProviderImpl(scopedContext())
            }
        }
    }

    @Test
    fun verifySingleInitializes() {
        load(false)
        assertEquals(0, initializedCount)
        assertEquals(0, terminatedCount)

        val child = childContext()
        val data = child.injectData(descriptor)
        assertEquals("Data!", data.get())
        assertEquals(1, initializedCount)
        assertEquals(0, terminatedCount)

        child.terminate()
        // single not destoys when client context destroyed
        assertEquals(1, initializedCount)
        assertEquals(0, terminatedCount)
    }

    @Test
    fun verifyInitOnLoadAndReInit() {
        load(true)
        assertEquals(1, initializedCount)
        assertEquals(0, terminatedCount)

        val dispatcher = injectDataDispatcher(descriptor)
        dispatcher.resetData(OnlyData)
        assertEquals(2, initializedCount)
        assertEquals(1, terminatedCount)
    }

}