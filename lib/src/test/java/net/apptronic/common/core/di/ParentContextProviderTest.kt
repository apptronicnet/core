package net.apptronic.common.core.di

import net.apptronic.common.utils.TestContext
import net.apptronic.core.component.context.BasicContext
import net.apptronic.core.component.context.ComponentContext
import net.apptronic.core.component.di.ObjectNotFoundException
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
            objects().addModule(parentModule)
        }
    }

    private class ChildContext(parent: ComponentContext) : BasicContext(parent) {
        init {
            objects().addModule(childModule)
        }
    }

    private val parent = Context()
    private val child = ChildContext(parent)

    @Test
    fun parentShouldHave() {
        val one = parent.objects().get(OneDescriptor)
        val two = parent.objects().get(TwoDescriptor)
    }

    @Test
    fun childShouldHave() {
        val one = child.objects().get(OneDescriptor)
        val two = child.objects().get(TwoDescriptor)
        val three = child.objects().get(ThreeDescriptor)
        assert(one is OneChild)
    }

    @Test(expected = ObjectNotFoundException::class)
    fun parentShouldNotHave() {
        val three = parent.objects().get(ThreeDescriptor)
    }

}