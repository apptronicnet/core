package net.apptronic.core.base.core.di

import net.apptronic.core.base.utils.TestContext
import net.apptronic.core.component.context.SubContext
import net.apptronic.core.component.di.InjectionFailedException
import net.apptronic.core.component.di.createDescriptor
import net.apptronic.core.component.di.declareModule
import org.junit.Test

class ParentContextProviderTest {

    private companion object {

        open class One
        class Two
        class Three
        class OneChild : One()

        val OneDescriptor = createDescriptor<One>()
        val TwoDescriptor = createDescriptor<Two>()
        val ThreeDescriptor = createDescriptor<Three>()

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


    private class Context : TestContext() {
        init {
            getProvider().addModule(parentModule)
        }
    }

    private class ChildContext(parent: net.apptronic.core.component.context.Context) :
        SubContext(parent) {
        init {
            getProvider().addModule(childModule)
        }
    }

    private val parent = Context()
    private val child = ChildContext(parent)

    @Test
    fun parentShouldHave() {
        val one = parent.getProvider().inject(OneDescriptor)
        val two = parent.getProvider().inject(TwoDescriptor)
    }

    @Test
    fun childShouldHave() {
        val one = child.getProvider().inject(OneDescriptor)
        val two = child.getProvider().inject(TwoDescriptor)
        val three = child.getProvider().inject(ThreeDescriptor)
        assert(one is OneChild)
    }

    @Test(expected = InjectionFailedException::class)
    fun parentShouldNotHave() {
        val three = parent.getProvider().inject(ThreeDescriptor)
    }

}