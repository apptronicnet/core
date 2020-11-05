package net.apptronic.core.entity.entities

import net.apptronic.core.assertListEquals
import net.apptronic.core.context.component.Component
import net.apptronic.core.context.component.typedEvent
import net.apptronic.core.testutils.createTestContext
import org.junit.Test

class DistinctUntilChangedEntityTest {

    val component = Component(createTestContext())
    val source = component.typedEvent<String>()
    val results = mutableListOf<String>()

    init {
        source.distinctUntilChanged().subscribe {
            results.add(it)
        }
    }

    @Test
    fun verifyDistinctUntilChanged() {
        source.sendEvent("One")
        assertListEquals(results, listOf("One"))

        source.sendEvent("Two")
        assertListEquals(results, listOf("One", "Two"))

        source.sendEvent("Two")
        assertListEquals(results, listOf("One", "Two"))

        source.sendEvent("Three")
        assertListEquals(results, listOf("One", "Two", "Three"))

        source.sendEvent("Three")
        assertListEquals(results, listOf("One", "Two", "Three"))

        source.sendEvent("Three")
        assertListEquals(results, listOf("One", "Two", "Three"))

        source.sendEvent("Three")
        assertListEquals(results, listOf("One", "Two", "Three"))

        source.sendEvent("Two")
        assertListEquals(results, listOf("One", "Two", "Three", "Two"))

        source.sendEvent("Two")
        assertListEquals(results, listOf("One", "Two", "Three", "Two"))

        source.sendEvent("One")
        assertListEquals(results, listOf("One", "Two", "Three", "Two", "One"))
    }

}