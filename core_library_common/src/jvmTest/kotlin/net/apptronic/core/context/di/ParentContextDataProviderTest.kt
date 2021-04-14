package net.apptronic.core.context.di

import net.apptronic.core.context.childContext
import net.apptronic.core.context.dependencyModule
import net.apptronic.core.testutils.createTestContext
import org.junit.Test

class ParentContextDataProviderTest {

    private companion object {

        open class One
        class Two
        class Three
        class OneChild : One()

        val OneDescriptor = dependencyDescriptor<One>()
        val TwoDescriptor = dependencyDescriptor<Two>()
        val ThreeDescriptor = dependencyDescriptor<Three>()

        val parentModule = declareModule {

            factory(OneDescriptor) {
                One()
            }

            factory(TwoDescriptor) {
                Two()
            }

        }

        val childModule = declareModule {

            factory(OneDescriptor) {
                OneChild()
            }

            factory(ThreeDescriptor) {
                Three()
            }

        }

    }

    private val parent = createTestContext {
        dependencyDispatcher.addModule(parentModule)
    }
    private val child = parent.childContext {
        dependencyModule(childModule)
    }

    @Test
    fun parentShouldHave() {
        val one = parent.dependencyDispatcher.inject(OneDescriptor)
        val two = parent.dependencyDispatcher.inject(TwoDescriptor)
    }

    @Test
    fun childShouldHave() {
        val one = child.dependencyDispatcher.inject(OneDescriptor)
        val two = child.dependencyDispatcher.inject(TwoDescriptor)
        val three = child.dependencyDispatcher.inject(ThreeDescriptor)
        assert(one is OneChild)
    }

    @Test(expected = InjectionFailedException::class)
    fun parentShouldNotHave() {
        val three = parent.dependencyDispatcher.inject(ThreeDescriptor)
    }

    @Test
    fun traceTest() {
        child.dependencyDispatcher.traceDependencyTree().print()
    }

}