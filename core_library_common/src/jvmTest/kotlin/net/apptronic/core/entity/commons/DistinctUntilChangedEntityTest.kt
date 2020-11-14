package net.apptronic.core.entity.commons

import net.apptronic.core.assertListEquals
import net.apptronic.core.context.component.Component
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
        source.update("One")
        assertListEquals(results, listOf("One"))

        source.update("Two")
        assertListEquals(results, listOf("One", "Two"))

        source.update("Two")
        assertListEquals(results, listOf("One", "Two"))

        source.update("Three")
        assertListEquals(results, listOf("One", "Two", "Three"))

        source.update("Three")
        assertListEquals(results, listOf("One", "Two", "Three"))

        source.update("Three")
        assertListEquals(results, listOf("One", "Two", "Three"))

        source.update("Three")
        assertListEquals(results, listOf("One", "Two", "Three"))

        source.update("Two")
        assertListEquals(results, listOf("One", "Two", "Three", "Two"))

        source.update("Two")
        assertListEquals(results, listOf("One", "Two", "Three", "Two"))

        source.update("One")
        assertListEquals(results, listOf("One", "Two", "Three", "Two", "One"))
    }

}