package net.apptronic.core.component.entity.entities

import net.apptronic.core.assertListEquals
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.component.extensions.BaseComponent
import net.apptronic.core.component.typedEvent
import net.apptronic.core.testutils.testContext
import org.junit.Test

class DistinctUntilChangedEntityTest {

    val component = BaseComponent(testContext())
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