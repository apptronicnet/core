package net.apptronic.core.context.di

import net.apptronic.core.context.EmptyContext
import net.apptronic.core.context.component.Component
import net.apptronic.core.context.lifecycle.enterStage
import net.apptronic.core.context.lifecycle.exitStage
import net.apptronic.core.context.terminate
import net.apptronic.core.testutils.TestLifecycle
import net.apptronic.core.testutils.createTestContext
import org.junit.Test

class SharedScopeProviderTest {

    private class Shared : AutoRecycling {
        var isRecycled = false
        override fun onAutoRecycle() {
            isRecycled = true
        }
    }

    private val module = declareModule {
        shared {
            Shared()
        }
    }

    private val context = createTestContext {
        dependencyDispatcher.addModule(module)
    }
    private val component = Component(context)

    @Test
    fun shouldBeSame() {
        val instance1 = component.inject<Shared>()
        val instance2 = component.inject<Shared>()
        val instance3 = component.inject<Shared>()
        assert(instance1 === instance2)
        assert(instance2 === instance3)
        assert(instance1 === instance3)
        assert(instance1.isRecycled.not())
    }

    @Test
    fun shouldBeDifferent() {
        enterStage(context, TestLifecycle.STAGE_CREATED)
        val instance1 = component.inject<Shared>()
        assert(instance1.isRecycled.not())
        exitStage(context, TestLifecycle.STAGE_CREATED)
        assert(instance1.isRecycled)

        enterStage(context, TestLifecycle.STAGE_CREATED)
        val instance2 = component.inject<Shared>()
        assert(instance2.isRecycled.not())
        exitStage(context, TestLifecycle.STAGE_CREATED)
        assert(instance2.isRecycled)

        enterStage(context, TestLifecycle.STAGE_CREATED)
        val instance3 = component.inject<Shared>()
        assert(instance3.isRecycled.not())
        exitStage(context, TestLifecycle.STAGE_CREATED)
        assert(instance3.isRecycled)

        assert(instance1 !== instance2)
        assert(instance2 !== instance3)
        assert(instance1 !== instance3)
    }

    @Test
    fun shouldBeSameInContexts() {
        val childContext1 = EmptyContext.createContext(context)
        val childComponent1 = Component(childContext1)
        val instance1 = childComponent1.inject<Shared>()
        assert(instance1.isRecycled.not())

        val childContext2 = EmptyContext.createContext(context)
        val childComponent2 = Component(childContext2)
        val instance2 = childComponent2.inject<Shared>()
        assert(instance1 === instance2)
        assert(instance1.isRecycled.not())

        childContext1.terminate()
        assert(instance1.isRecycled.not())

        childContext2.terminate()
        assert(instance1.isRecycled)
    }

    @Test
    fun shouldBeDifferentInContexts() {
        val childContext1 = EmptyContext.createContext(context)
        val childComponent1 = Component(childContext1)
        val instance1 = childComponent1.inject<Shared>()
        assert(instance1.isRecycled.not())
        childContext1.terminate()
        assert(instance1.isRecycled)

        val childContext2 = EmptyContext.createContext(context)
        val childComponent2 = Component(childContext2)
        val instance2 = childComponent2.inject<Shared>()
        assert(instance1 !== instance2)
        assert(instance2.isRecycled.not())
        childContext2.terminate()
        assert(instance2.isRecycled)
    }

    @Test
    fun traceTest() {
        context.dependencyDispatcher.traceDependencyTree().print()
    }

}