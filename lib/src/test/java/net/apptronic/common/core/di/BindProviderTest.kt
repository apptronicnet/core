package net.apptronic.common.core.di

import net.apptronic.common.utils.TestContext
import net.apptronic.core.component.di.alsoAs
import net.apptronic.core.component.di.createDescriptor
import net.apptronic.core.component.di.declareModule
import org.junit.Test

class BindProviderTest {

    private companion object {

        interface One
        interface Two

        class Impl : One, Two

        val OneDescriptor = createDescriptor<One>()
        val TwoDescriptor = createDescriptor<Two>()

        val module = declareModule {

            factory<One>(OneDescriptor) {
                Impl()
            }

            bind<Two>(OneDescriptor alsoAs TwoDescriptor)

        }
    }


    private class Context : TestContext() {
        init {
            getProvider().addModule(module)
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

}