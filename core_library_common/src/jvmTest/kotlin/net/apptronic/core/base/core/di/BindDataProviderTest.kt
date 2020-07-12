package net.apptronic.core.base.core.di

import net.apptronic.core.component.di.alsoAs
import net.apptronic.core.component.di.declareModule
import net.apptronic.core.component.di.dependencyDescriptor
import net.apptronic.core.testutils.testContext
import org.junit.Test

class BindDataProviderTest {

    private companion object {

        interface One
        interface Two

        class Impl : One, Two

        val OneDescriptor = dependencyDescriptor<One>()
        val TwoDescriptor = dependencyDescriptor<Two>()

        val module = declareModule {

            factory<One>(OneDescriptor) {
                Impl()
            }

            bind<Two>(OneDescriptor alsoAs TwoDescriptor)

        }
    }


    @Test
    fun shouldDoBind() {
//        val context = Context()
//        val one = context.getProvider().inject(OneDescriptor)
//        val two = context.getProvider().inject(TwoDescriptor)
//        assert(one is Impl)
//        assert(two is Impl)
    }

    @Test
    fun traceTest() {
        val context = testContext {
            dependencyDispatcher.addModule(module)
        }
        context.dependencyDispatcher.traceDependencyTree().print()
    }

}