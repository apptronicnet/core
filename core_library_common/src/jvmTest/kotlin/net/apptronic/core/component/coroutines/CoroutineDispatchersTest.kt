package net.apptronic.core.component.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import net.apptronic.core.testutils.testContext
import org.junit.Test
import kotlin.coroutines.CoroutineContext
import kotlin.test.assertSame

class CoroutineDispatchersTest {

    class SomeDispatcher : CoroutineDispatcher() {
        override fun dispatch(context: CoroutineContext, block: Runnable) {
            block.run()
        }
    }

    private val CustomDescriptor = coroutineDispatcherDescriptor("Custom")
    private val NonExistsDescriptor = coroutineDispatcherDescriptor("NonExits")

    val fallback = SomeDispatcher()
    val main = SomeDispatcher()
    val default = SomeDispatcher()
    val uncorfined = SomeDispatcher()
    val priority = BackgroundPriorityDispatcher(Dispatchers.Unconfined, Dispatchers.Unconfined)
    val manual = ManualDispatcher()
    val custom = ManualDispatcher()

    val context = testContext(
            coroutineDispatchers = CoroutineDispatchers(fallback).also {
                it[MainDispatcherDescriptor] = main
                it[DefatultDispatcherDescriptor] = default
                it[UnconfinedDispatcherDescriptor] = uncorfined
                it[BackgroundPriorityDispatcherDescriptor] = priority
                it[ManualDispatcherDescriptor] = manual
                it[CustomDescriptor] = custom
            }
    )

    @ExperimentalStdlibApi
    @Test
    fun verifyDispatchers() {
        assertSame(context.mainDispatcher, main)
        assertSame(context.defaultDispatcher, default)
        assertSame(context.unconfinedDispatcher, uncorfined)
        assertSame(context.manualDispatcher, manual)
        assertSame(context.coroutineDispatcher(CustomDescriptor), custom)
        assertSame(context.coroutineDispatcher(NonExistsDescriptor), fallback)
        val priorityContext = context.priorityDispatcher(PRIORITY_MEDIUM)
        assertSame<Any>(priorityContext[CoroutineDispatcher.Key] as Any, priority)
    }

}