package net.apptronic.core.base.core.di

import net.apptronic.core.component.context.EmptyContext
import net.apptronic.core.component.context.terminate
import net.apptronic.core.component.di.AutoRecycling
import net.apptronic.core.component.di.declareModule
import net.apptronic.core.component.extensions.BaseComponent
import net.apptronic.core.component.inject
import net.apptronic.core.component.lifecycle.enterStage
import net.apptronic.core.component.lifecycle.exitStage
import net.apptronic.core.testutils.TestLifecycle
import net.apptronic.core.testutils.testContext
import org.junit.Test

class SharedDataProviderTest {

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

    private val context = testContext {
        dependencyDispatcher.addModule(module)
    }
    private val component = BaseComponent(context)

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
        val childComponent1 = BaseComponent(childContext1)
        val instance1 = childComponent1.inject<Shared>()
        assert(instance1.isRecycled.not())

        val childContext2 = EmptyContext.createContext(context)
        val childComponent2 = BaseComponent(childContext2)
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
        val childComponent1 = BaseComponent(childContext1)
        val instance1 = childComponent1.inject<Shared>()
        assert(instance1.isRecycled.not())
        childContext1.terminate()
        assert(instance1.isRecycled)

        val childContext2 = EmptyContext.createContext(context)
        val childComponent2 = BaseComponent(childContext2)
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