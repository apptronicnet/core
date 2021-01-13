package net.apptronic.core.context.di

import junit.framework.Assert.assertNotSame
import kotlinx.coroutines.test.TestCoroutineDispatcher
import net.apptronic.core.BaseContextTest
import net.apptronic.core.base.initTestNanoTime
import net.apptronic.core.base.shiftTestTimeMillis
import net.apptronic.core.context.Contextual
import net.apptronic.core.context.childContext
import net.apptronic.core.context.coroutines.CoroutineDispatchers
import net.apptronic.core.context.dependencyModule
import net.apptronic.core.context.terminate
import net.apptronic.core.testutils.createTestContext
import org.junit.Test
import kotlin.test.assertSame

class SharedScopeFallbackTimeTest : BaseContextTest() {

    init {
        initTestNanoTime()
    }

    private val testDispatcher = TestCoroutineDispatcher()
    override val context = createTestContext(
        coroutineDispatchers = CoroutineDispatchers(testDispatcher)
    )

    val KeyDescriptor = dependencyDescriptor<Int>()

    inner class SharedObject(val key: Int) {
        override fun toString(): String {
            return super.toString() + "/key=$key"
        }
    }

    init {
        context.dependencyModule {
            shared(
                fallbackCount = 999,
                fallbackLifetime = 20
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
    fun verifyFallbackTimer() {
        val child1 = context.childContext()

        val shared1_1 = child1.sharedObject(1)
        val shared2_1 = child1.sharedObject(2)

        child1.terminate()

        shiftTestTimeMillis(10)
        testDispatcher.advanceTimeBy(10)

        val child2 = context.childContext()

        val shared1_2 = child2.sharedObject(1)
        val shared2_2 = child2.sharedObject(2)

        assertSame(shared1_1, shared1_2)
        assertSame(shared2_1, shared2_2)

        child2.terminate()

        shiftTestTimeMillis(30)
        testDispatcher.advanceTimeBy(30)

        val child3 = context.childContext()

        val shared1_3 = child3.sharedObject(1)
        val shared2_3 = child3.sharedObject(2)

        assertNotSame(shared1_1, shared1_3)
        assertNotSame(shared2_1, shared2_3)
    }

}