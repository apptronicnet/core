package net.apptronic.core.context.di

import net.apptronic.core.context.Context
import net.apptronic.core.context.EmptyContext
import net.apptronic.core.context.component.Component
import net.apptronic.core.context.terminate
import net.apptronic.core.testutils.createTestContext
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
        val context = createTestContext {
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