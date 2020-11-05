package net.apptronic.core.entity.behavior

import net.apptronic.core.assertListEquals
import net.apptronic.core.context.component.Component
import net.apptronic.core.context.coroutines.CoroutineDispatchers
import net.apptronic.core.context.coroutines.ManualDispatcher
import net.apptronic.core.entity.commons.value
import net.apptronic.core.testutils.createTestContext
import org.junit.Test

class ThrottleAsyncEntityTest {

    val dispathcer = ManualDispatcher()
    val component = Component(createTestContext(
            coroutineDispatchers = CoroutineDispatchers(dispathcer)
    ))
    val source = component.value<String>()
    val results = mutableListOf<String>()

    init {
        source.throttleAsync().subscribe {
            results.add(it)
        }
    }

    @Test
    fun verifyThrottleAsync() {
        source.set("One")
        assertListEquals(results, listOf())

        dispathcer.runAll()
        assertListEquals(results, listOf("One"))

        source.set("Two")
        assertListEquals(results, listOf("One"))

        source.set("Three")
        assertListEquals(results, listOf("One"))

        source.set("Four")
        assertListEquals(results, listOf("One"))

        dispathcer.runAll()
        assertListEquals(results, listOf("One", "Four"))

        source.set("Five")
        dispathcer.runAll()
        assertListEquals(results, listOf("One", "Four", "Five"))
    }

}