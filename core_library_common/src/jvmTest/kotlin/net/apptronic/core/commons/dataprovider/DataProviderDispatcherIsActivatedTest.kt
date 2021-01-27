package net.apptronic.core.commons.dataprovider

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
import net.apptronic.core.BaseContextTest
import net.apptronic.core.context.Context
import net.apptronic.core.context.dependencyModule
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.commons.value
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.fail

class DataProviderDispatcherIsActivatedTest : BaseContextTest() {

    var dataSuffix = "A"

    val DataDescriptor = dataProviderDescriptor<Int, String>()

    var initializedCount = 0
    var terminatedCount = 0

    fun assertCounters(initialized: Int, terminated: Int) {
        assertEquals(initializedCount, initialized)
        assertEquals(terminatedCount, terminated)
    }

    inner class DataProviderImpl(context: Context, key: Int) : DataProvider<Int, String>(context, key) {

        init {
            initializedCount++
            doOnTerminate {
                terminatedCount++
            }
        }

        override val dataProviderEntity: Entity<String> = value("$key-$dataSuffix")

        override suspend fun loadData(): String {
            return "$key-$dataSuffix"
        }

    }

    init {
        context.dependencyModule {
            sharedDataProvider(DataDescriptor) { key ->
                DataProviderImpl(scopedContext(), key)
            }
        }
    }

    val dispatcher = injectDataDispatcher(DataDescriptor)

    @Test
    fun verifyDeactivatedNotProvidesData() {
        assertCounters(0, 0)
        dispatcher.isActivated = false
        val data = injectData(DataDescriptor, 1)
        assertCounters(0, 0)
        assertFalse(data.isSet())
    }

    @Test
    fun verifyActivatedNotLosesDataOnDeactivate() {
        assertCounters(0, 0)
        val data = injectData(DataDescriptor, 1)
        assertEquals(data.get(), "1-A")
        assertCounters(1, 0)
        dispatcher.isActivated = false
        assertCounters(1, 1)
        assertEquals(data.get(), "1-A")
    }

    @Test
    fun verifyDataAppearsOnActivation() {
        assertCounters(0, 0)
        dispatcher.isActivated = false
        val data = injectData(DataDescriptor, 1)
        assertCounters(0, 0)
        assertFalse(data.isSet())
        dispatcher.isActivated = true
        assertCounters(1, 0)
        assertEquals(data.get(), "1-A")
    }

    @Test
    fun verifyDataReloadsOnAfterReactivation() {
        assertCounters(0, 0)
        dispatcher.isActivated = false
        val data = injectData(DataDescriptor, 1)
        assertCounters(0, 0)
        dataSuffix = "B"
        dispatcher.isActivated = true
        assertCounters(1, 0)
        assertEquals(data.get(), "1-B")
    }

    @Test
    fun verifyReloadFailsForDeactivated() {
        assertCounters(0, 0)
        dispatcher.isActivated = false
        val data = injectData(DataDescriptor, 1)
        assertCounters(0, 0)
        runBlocking {
            try {
                data.reload()
                fail("Cannot reload on deactivated data provider")
            } catch (e: CancellationException) {
                assertEquals(e.message, "DataProvider is not deactivated")
            }
        }
    }

    @Test
    fun verifyReloadSuccessOnReactivate() {
        assertCounters(0, 0)
        dispatcher.isActivated = false
        val data = injectData(DataDescriptor, 1)
        assertCounters(0, 0)
        dispatcher.isActivated = true
        assertCounters(1, 0)
        assertEquals(data.get(), "1-A")
        dataSuffix = "B"
        runBlocking {
            assertEquals(data.reload(), "1-B")
        }
        assertEquals(data.get(), "1-B")
    }

    @Test
    fun verifyActivateDeactivateMultipleTimes() {
        assertCounters(0, 0)
        val data = injectData(DataDescriptor, 1)
        assertCounters(1, 0)
        assertEquals(data.get(), "1-A")

        dispatcher.isActivated = false
        assertCounters(1, 1)

        dataSuffix = "B"
        assertEquals(data.get(), "1-A")
        dispatcher.isActivated = true
        assertCounters(2, 1)
        assertEquals(data.get(), "1-B")

        dispatcher.isActivated = false
        assertCounters(2, 2)

        dataSuffix = "C"
        assertEquals(data.get(), "1-B")
        dispatcher.isActivated = true
        assertCounters(3, 2)
        assertEquals(data.get(), "1-C")

        dispatcher.isActivated = false
        assertCounters(3, 3)

        dataSuffix = "End"
        assertEquals(data.get(), "1-C")
        dispatcher.isActivated = true
        assertCounters(4, 3)
        assertEquals(data.get(), "1-End")
    }

    @Test
    fun verifyDeactivateActivateMultipleProviders() {
        assertCounters(0, 0)
        val data1 = injectData(DataDescriptor, 1)
        val data2 = injectData(DataDescriptor, 2)
        assertCounters(2, 0)
        assertEquals(data1.get(), "1-A")
        assertEquals(data2.get(), "2-A")

        dispatcher.isActivated = false
        assertCounters(2, 2)

        val data3 = injectData(DataDescriptor, 3)
        val data4 = injectData(DataDescriptor, 4)
        assertCounters(2, 2)
        assertFalse(data3.isSet())
        assertFalse(data4.isSet())

        dispatcher.isActivated = true
        assertCounters(6, 2)
        assertEquals(data3.get(), "3-A")
        assertEquals(data4.get(), "4-A")

        dispatcher.isActivated = false
        assertCounters(6, 6)

        dataSuffix = "Changed"
        dispatcher.isActivated = true
        assertCounters(10, 6)
        assertEquals(data1.get(), "1-Changed")
        assertEquals(data2.get(), "2-Changed")
        assertEquals(data3.get(), "3-Changed")
        assertEquals(data4.get(), "4-Changed")
    }

}