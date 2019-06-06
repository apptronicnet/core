package net.apptronic.core.base.core.di

import net.apptronic.core.base.utils.TestContext
import net.apptronic.core.component.Component
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.di.AutoRecycling
import net.apptronic.core.component.di.createDescriptor
import net.apptronic.core.component.di.declareModule
import org.junit.Test

class AutoRecyclingTest {

    interface Declaration {

    }

    class Implementation : Declaration, AutoRecycling {

        var isRecycled = false

        override fun onAutoRecycle() {
            isRecycled = true
        }

    }

    companion object {

        val DeclarationDescriptor = createDescriptor<Declaration>()

        val module = declareModule {

            single(DeclarationDescriptor) {
                Implementation()
            }

        }

    }

    class TestComponent(context: Context) : Component(context) {

        val instance = getProvider().inject(DeclarationDescriptor)

    }

    @Test
    fun shouldAutoRecycle() {
        val context = TestContext()
        context.getProvider().addModule(module)
        val component = TestComponent(context)
        assert(component.instance is Implementation)
        assert((component.instance as Implementation).isRecycled.not())

        context.getLifecycle().terminate()
        assert((component.instance).isRecycled)
    }

}