package net.apptronic.core.context.di

import junit.framework.Assert.assertNotSame
import net.apptronic.core.BaseContextTest
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.childContext
import net.apptronic.core.context.dependencyModule
import net.apptronic.core.context.terminate
import org.junit.Test
import kotlin.test.assertSame

class SharedScopeFallbackCounterTest : BaseContextTest() {

    val KeyDescriptor = dependencyDescriptor<Int>()

    inner class SharedObject(val key: Int) {
        override fun toString(): String {
            return super.toString() + "/key=$key"
        }
    }

    init {
        context.dependencyModule {
            shared(
                fallbackCount = 3,
                fallbackLifetime = 999_999
            ) {
                SharedObject(provided(KeyDescriptor))
            }
        }
    }

    private fun Contextual.sharedObject(key: Int) = dependencyProvider.inject<SharedObject>(
        parameters {
            KeyDescriptor withValue key
        }
    )

    @Test
    fun verifyFallbackCounter() {
        val child1 = context.childContext()

        val shared1_1 = child1.sharedObject(1)
        val shared2_1 = child1.sharedObject(2)
        val shared3_1 = child1.sharedObject(3)
        val shared4_1 = child1.sharedObject(4)

        child1.terminate()

        val child2 = context.childContext()

        val shared1_2 = child2.sharedObject(1)
        val shared2_2 = child2.sharedObject(2)
        val shared3_2 = child2.sharedObject(3)
        val shared4_2 = child2.sharedObject(4)

        assertNotSame(shared1_1, shared1_2)
        assertSame(shared2_1, shared2_2)
        assertSame(shared3_1, shared3_2)
        assertSame(shared4_1, shared4_2)
    }

}