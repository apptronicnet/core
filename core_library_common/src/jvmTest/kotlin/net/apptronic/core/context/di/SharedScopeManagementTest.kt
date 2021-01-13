package net.apptronic.core.context.di

import net.apptronic.core.BaseContextTest
import net.apptronic.core.context.*
import net.apptronic.core.context.component.Component
import net.apptronic.core.entity.commons.genericEvent
import net.apptronic.core.entity.commons.typedEvent
import org.junit.Test
import kotlin.test.assertNotSame
import kotlin.test.assertSame

class SharedScopeManagementTest : BaseContextTest() {

    private class ManagerImpl(context: Context) : Component(context), SharedScopeManager {

        val resetAllEvent = genericEvent()
        val resetOneEvent = typedEvent<Int>()

        override fun onSharedScopeInitialized(owner: SharedScopeOwner) {
            resetAllEvent.subscribe {
                owner.resetAllFallbacks()
            }
            resetOneEvent.subscribe {
                owner.resetFallback(parametersOf(it))
            }
        }

    }

    private val managerDescriptor = dependencyDescriptor<ManagerImpl>()
    private val objectDescriptor = dependencyDescriptor<SomeObject>()

    private class SomeObject

    init {
        context.dependencyModule {
            single(managerDescriptor) {
                ManagerImpl(scopedContext())
            }
            shared(
                objectDescriptor,
                fallbackCount = 100,
                fallbackLifetime = 999_999,
                managerDescriptor = managerDescriptor
            ) {
                SomeObject()
            }
        }
    }

    private val manager = dependencyProvider.inject(managerDescriptor)

    private fun Contextual.injectObject(key: Int): SomeObject {
        return dependencyProvider.inject(objectDescriptor, parametersOf(withValue(key)))
    }

    @Test
    fun verifyResetAllFallbacks() {
        val child1 = childContext()

        val val1_1 = child1.injectObject(1)
        val val2_1 = child1.injectObject(2)

        child1.terminate()

        val child2 = childContext()

        val val1_2 = child2.injectObject(1)
        val val2_2 = child2.injectObject(2)
        assertSame(val1_1, val1_2)
        assertSame(val2_1, val2_2)

        child2.terminate()

        manager.resetAllEvent.update()

        val child3 = childContext()
        val val1_3 = child3.injectObject(1)
        val val2_3 = child3.injectObject(2)
        assertNotSame(val1_1, val1_3)
        assertNotSame(val2_1, val2_3)
    }

    @Test
    fun verifyResetAllKeepsUsed() {
        val child1 = childContext()
        val child2 = childContext()

        val val1_1 = child1.injectObject(1)

        val val1_2 = child2.injectObject(1)
        val val2_2 = child2.injectObject(2)

        child2.terminate()

        manager.resetAllEvent.update()

        val child3 = childContext()
        val val1_3 = child3.injectObject(1)
        val val2_3 = child3.injectObject(2)

        assertSame(val1_1, val1_3)
        assertSame(val1_2, val1_3)
        assertNotSame(val2_2, val2_3)
    }

    @Test
    fun verifyResetSingleFallback() {
        val child1 = childContext()

        val val1_1 = child1.injectObject(1)
        val val2_1 = child1.injectObject(2)

        child1.terminate()

        val child2 = childContext()

        val val1_2 = child2.injectObject(1)
        val val2_2 = child2.injectObject(2)
        assertSame(val1_1, val1_2)
        assertSame(val2_1, val2_2)

        child2.terminate()

        manager.resetOneEvent.update(1)

        val child3 = childContext()
        val val1_3 = child3.injectObject(1)
        val val2_3 = child3.injectObject(2)
        assertNotSame(val1_1, val1_3)
        assertSame(val2_1, val2_3)
    }

    @Test
    fun verifyResetSingleKeepsUsed() {
        val child1 = childContext()
        val child2 = childContext()

        val val1_1 = child1.injectObject(1) // kept used
        val val2_1 = child1.injectObject(2)

        val val1_2 = child2.injectObject(1)
        val val2_2 = child2.injectObject(2)

        child2.terminate()

        manager.resetOneEvent.update(1)

        val child3 = childContext()
        val val1_3 = child3.injectObject(1)
        val val2_3 = child3.injectObject(2)

        assertSame(val1_1, val1_3)
        assertSame(val1_2, val1_3)
        assertSame(val2_1, val2_3)
        assertSame(val2_2, val2_3)
    }

}