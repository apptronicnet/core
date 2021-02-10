package net.apptronic.core.context.di

import net.apptronic.core.context.EmptyContext
import net.apptronic.core.context.component.Component
import net.apptronic.core.testutils.createTestContext
import org.junit.Before
import org.junit.Test

class ProvidingTest {

    private companion object {
        const val EXPECTED_TEXT = "Some text"
        val SomeThingProvidedDescriptor = dependencyDescriptor<SomeThing>()
        val SomeThingDescriptor = dependencyDescriptor<SomeThing>()
        val SomeThingTextDescriptor = dependencyDescriptor<String>()
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

    class SomeThing(val text: String)

    lateinit var component: Component

    @Before
    fun before() {
        val coreContext = createTestContext {
            dependencyDispatcher.addModule(CoreModule)
        }
        component = Component(coreContext, EmptyContext)
        component.context.dependencyDispatcher.addInstance(SomeThingTextDescriptor, EXPECTED_TEXT)
    }

    @Test(expected = InjectionFailedException::class)
    fun shouldFailToFindText() {
        component.inject(SomeThingDescriptor)
    }

    @Test
    fun shouldFindText() {
        val result = component.inject(SomeThingProvidedDescriptor)
        assert(result.text == EXPECTED_TEXT)
    }

    @Test
    fun traceTest() {
        component.context.dependencyDispatcher.traceDependencyTree().print()
    }

}