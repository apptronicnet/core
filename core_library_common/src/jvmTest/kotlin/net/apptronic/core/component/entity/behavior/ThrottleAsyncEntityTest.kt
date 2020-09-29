package net.apptronic.core.component.entity.behavior

import net.apptronic.core.assertListEquals
import net.apptronic.core.component.coroutines.CoroutineDispatchers
import net.apptronic.core.component.coroutines.ManualDispatcher
import net.apptronic.core.component.extensions.BaseComponent
import net.apptronic.core.component.value
import net.apptronic.core.testutils.testContext
import org.junit.Test

class ThrottleAsyncEntityTest {

    val dispathcer = ManualDispatcher()
    val component = BaseComponent(testContext(
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