package net.apptronic.core.component.entity.entities

import net.apptronic.core.assertListEquals
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.component.extensions.BaseComponent
import net.apptronic.core.component.typedEvent
import net.apptronic.core.testutils.testContext
import org.junit.Test

class EventTest {

    val component = BaseComponent(testContext())
    val source = component.typedEvent<String>()
    val results = mutableListOf<String>()

    init {
        source.subscribe {
            results.add(it)
        }
    }

    @Test
    fun verifyDistinctUntilChanged() {
        source.update("One")
        assertListEquals(results, listOf("One"))

        source.update("Two")
        assertListEquals(results, listOf("One", "Two"))

        source.update("Two")
        assertListEquals(results, listOf("One", "Two", "Two"))

        source.update("Three")
        assertListEquals(results, listOf("One", "Two", "Two", "Three"))

        source.update("Three")
        assertListEquals(results, listOf("One", "Two", "Two", "Three", "Three"))

        source.update("Three")
        assertListEquals(results, listOf("One", "Two", "Two", "Three", "Three", "Three"))

        source.update("Three")
        assertListEquals(results, listOf("One", "Two", "Two", "Three", "Three", "Three", "Three"))

        source.update("Two")
        assertListEquals(results, listOf("One", "Two", "Two", "Three", "Three", "Three", "Three", "Two"))

        source.update("Two")
        assertListEquals(results, listOf("One", "Two", "Two", "Three", "Three", "Three", "Three", "Two", "Two"))

        source.update("One")
        assertListEquals(results, listOf("One", "Two", "Two", "Three", "Three", "Three", "Three", "Two", "Two", "One"))
    }

}