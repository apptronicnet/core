package net.apptronic.core.commons.dataprovider

import kotlinx.coroutines.withContext
import net.apptronic.core.commons.cache.SimpleCache
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.childContext
import net.apptronic.core.component.context.dependencyModule
import net.apptronic.core.component.coroutines.ManualDispatcher
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.entities.asProperty
import net.apptronic.core.component.entity.functions.mapSuspend
import net.apptronic.core.component.extensions.BaseComponent
import net.apptronic.core.component.now
import net.apptronic.core.component.terminate
import net.apptronic.core.testutils.testContext
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

private val DataProviderDescriptor = dataProviderDescriptor<DataProviderWithCacheTest.Data, Int>()

class DataProviderWithCacheTest {

    class Data(val value: String)

    val manualDispatcher = ManualDispatcher()

    inner class Provider(context: Context, key: Int) : DataProvider<Data, Int>(context, key) {

        override val entity: Entity<Data> = now().mapSuspend {
            withContext(manualDispatcher) {
                Data(key.toString())
            }
        }

    }

    val context = testContext {
        dependencyModule {
            sharedCache(DataProviderDescriptor) {
                SimpleCache(scopedContext(), maxCount = 2)
            }
            sharedDataProvider(DataProviderDescriptor) {
                Provider(scopedContext(), it)
            }
        }
    }

    private fun userComponent(id: Int) = UserComponent(context.childContext(), id)

    class UserComponent(context: Context, val id: Int) : BaseComponent(context) {
        val data = injectData(DataProviderDescriptor, id).asProperty()
    }

    @Test
    fun shouldGetFromCache() {
        val user1_1 = userComponent(1)
        assertFalse(user1_1.data.isSet())

        manualDispatcher.runAll()

        assertTrue(user1_1.data.isSet())

        val user1_2 = userComponent(1)
        assertTrue(user1_1.data.isSet())
        assertTrue(user1_1.data.get() === user1_2.data.get())

        user1_1.terminate()
        user1_2.terminate()

        val user1_3 = userComponent(1)
        assertTrue(user1_3.data.isSet())

        // get same instance from cache
        assertTrue(user1_1.data.get() === user1_3.data.get())

        manualDispatcher.runAll()

        // instance replaced by new after loading
        assertFalse(user1_1.data.get() === user1_3.data.get())

        user1_3.terminate()

        // load 2 new items to override cache
        val user2 = userComponent(2)
        val user3 = userComponent(3)
        manualDispatcher.runAll()
        assertTrue(user2.data.isSet())
        assertTrue(user3.data.isSet())
        // and terminate them
        user2.terminate()
        user3.terminate()

        // cache for id #1 should be erased at this moment so notyhing returned from cache
        val user1_4 = userComponent(1)
        assertFalse(user1_4.data.isSet())

        manualDispatcher.runAll()

        // new instance returned from loading
        assertTrue(user1_4.data.isSet())
        assertTrue(user1_4.data.get() !== user1_1.data.get())
        assertTrue(user1_4.data.get() !== user1_2.data.get())
        assertTrue(user1_4.data.get() !== user1_3.data.get())
    }

}