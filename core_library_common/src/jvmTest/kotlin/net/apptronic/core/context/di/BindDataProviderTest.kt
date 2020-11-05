package net.apptronic.core.context.di

import net.apptronic.core.testutils.createTestContext
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
        val context = createTestContext {
            dependencyDispatcher.addModule(module)
        }
        context.dependencyDispatcher.traceDependencyTree().print()
    }

}