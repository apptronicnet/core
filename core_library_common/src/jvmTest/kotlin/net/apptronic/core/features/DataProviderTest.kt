package net.apptronic.core.features

import net.apptronic.core.assertListEquals
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.EmptyContext
import net.apptronic.core.component.context.childContext
import net.apptronic.core.component.context.dependencyModule
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.behavior.filter
import net.apptronic.core.component.entity.behavior.filterNotNull
import net.apptronic.core.component.entity.behavior.resendWhen
import net.apptronic.core.component.entity.behavior.switchContext
import net.apptronic.core.component.entity.entities.asProperty
import net.apptronic.core.component.entity.functions.map
import net.apptronic.core.component.extensions.BaseComponent
import net.apptronic.core.component.inject
import net.apptronic.core.component.newChain
import net.apptronic.core.component.terminate
import net.apptronic.core.component.typedEvent
import net.apptronic.core.features.provider.DataProvider
import net.apptronic.core.features.provider.dataProviderDescriptor
import net.apptronic.core.features.provider.injectData
import net.apptronic.core.features.provider.sharedDataProvider
import net.apptronic.core.testutils.testContext
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class DataProviderTest {

    val StringProviderDescriptor = dataProviderDescriptor<String, Int>()

    private val providersById = mutableListOf<Int>()

    private class Repository(context: Context) : BaseComponent(context) {

        private val onUpdateById = typedEvent<Int>()
        private val storage = mutableMapOf<Int, String>()

        fun saveById(id: Int, data: String) {
            storage[id] = data
            onUpdateById.sendEvent(id)
        }

        fun loadById(id: Int): String? {
            return storage[id]
        }

        fun observeUpdatedById(context: Context): Entity<Int> {
            return onUpdateById.switchContext(context)
        }

    }

    private inner class StringValueDataProvider(context: Context, id: Int) : DataProvider<String>(context) {

        init {
            providersById.add(id)
            doOnTerminate {
                providersById.remove(id)
            }
        }

        private val repository = inject<Repository>()

        override val entity: Entity<String> = newChain()
                .resendWhen(
                        repository.observeUpdatedById(context).filter { it == id }
                ).map {
                    repository.loadById(id)
                }.filterNotNull()

    }

    val context = testContext {
        dependencyModule {
            single {
                Repository(scopedContext(EmptyContext))
            }
            sharedDataProvider<String, Int>(StringProviderDescriptor) {
                StringValueDataProvider(
                        scopedContext(EmptyContext), it
                )
            }
        }
    }

    private inner class UsesStringComponent(context: Context, val id: Int) : BaseComponent(context) {

        val data = injectData(StringProviderDescriptor, id).asProperty()

    }

    @Test
    fun verifySharedProvider() {
        val repository = context.inject<Repository>()
        repository.saveById(1, "One Thing")

        val uses1_1 = UsesStringComponent(context.childContext(), 1)
        assertListEquals(providersById, listOf(1))
        assertEquals(uses1_1.data.get(), "One Thing")

        val uses1_2 = UsesStringComponent(context.childContext(), 1)
        assertListEquals(providersById, listOf(1))
        assertEquals(uses1_2.data.get(), "One Thing")

        repository.saveById(1, "One Doctor")

        assertEquals(uses1_1.data.get(), "One Doctor")
        assertEquals(uses1_2.data.get(), "One Doctor")

        val uses2 = UsesStringComponent(context.childContext(), 2)
        assertListEquals(providersById, listOf(1, 2))
        assertFalse(uses2.data.isSet())

        repository.saveById(2, "Two Promo")
        assertEquals(uses2.data.get(), "Two Promo")
        uses1_1.terminate()

        assertListEquals(providersById, listOf(1, 2))
        repository.saveById(1, "One Fact")
        assertEquals(uses1_1.data.get(), "One Doctor") // not changed as was terminated
        assertEquals(uses1_2.data.get(), "One Fact")

        uses1_2.terminate()
        assertListEquals(providersById, listOf(2))
    }

}