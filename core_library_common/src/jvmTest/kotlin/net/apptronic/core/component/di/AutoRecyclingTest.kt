package net.apptronic.core.component.di

import net.apptronic.core.component.Component
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.EmptyContext
import net.apptronic.core.component.context.terminate
import net.apptronic.core.component.inject
import net.apptronic.core.testutils.testContext
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

        val DeclarationDescriptor = dependencyDescriptor<Declaration>()

        val module = declareModule {

            single(DeclarationDescriptor) {
                Implementation()
            }

        }

    }

    class TestComponent(context: Context) : Component(context, EmptyContext) {

        val instance = inject(DeclarationDescriptor)

    }

    @Test
    fun shouldAutoRecycle() {
        val context = testContext {
            dependencyDispatcher.addModule(module)
            dependencyDispatcher.traceDependencyTree().print()
        }

        val component = TestComponent(context)
        assert(component.instance is Implementation)
        assert((component.instance as Implementation).isRecycled.not())

        context.terminate()
        assert((component.instance).isRecycled)
    }

}