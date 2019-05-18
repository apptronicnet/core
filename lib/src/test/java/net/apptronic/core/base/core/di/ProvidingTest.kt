package net.apptronic.core.base.core.di

import net.apptronic.core.base.utils.TestContext
import net.apptronic.core.component.Component
import net.apptronic.core.component.context.SubContext
import net.apptronic.core.component.di.InjectionFailedException
import net.apptronic.core.component.di.createDescriptor
import net.apptronic.core.component.di.declareModule
import org.junit.Before
import org.junit.Test

class ProvidingTest {

    private companion object {
        const val EXPECTED_TEXT = "Some text"
        val SomeThingProvidedDescriptor = createDescriptor<SomeThing>()
        val SomeThingDescriptor = createDescriptor<SomeThing>()
        val SomeThingTextDescriptor = createDescriptor<String>()
        val CoreModule = declareModule {

            factory(SomeThingProvidedDescriptor) {
                val text = provided(SomeThingTextDescriptor)
                SomeThing(text)
            }

            factory(SomeThingDescriptor) {
                val text = inject(SomeThingTextDescriptor)
                SomeThing(text)
            }

        }

    }

    class CoreContext : TestContext() {
        init {
            getProvider().addModule(CoreModule)
        }
    }

    class SomeThing(val text: String)

    lateinit var component: Component

    @Before
    fun before() {
        val coreContext = CoreContext()
        val childContext = SubContext(coreContext)
        component = Component(childContext)
        component.getProvider().addInstance(SomeThingTextDescriptor, EXPECTED_TEXT)
    }

    @Test(expected = InjectionFailedException::class)
    fun shouldFailToFindText() {
        component.getProvider().inject(SomeThingDescriptor)
    }

    @Test
    fun shouldFindText() {
        val result = component.getProvider().inject(SomeThingProvidedDescriptor)
        assert(result.text == EXPECTED_TEXT)
    }

}