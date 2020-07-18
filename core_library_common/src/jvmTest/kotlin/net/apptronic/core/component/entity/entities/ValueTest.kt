package net.apptronic.core.component.entity.entities

import net.apptronic.core.assertListEquals
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.component.extensions.BaseComponent
import net.apptronic.core.component.value
import net.apptronic.core.testutils.testContext
import org.junit.Test

class ValueTest {

    val component = BaseComponent(testContext())
    val source = component.value<String>()
    val results = mutableListOf<String>()

    init {
        source.subscribe {
            results.add(it)
        }
    }

    @Test
    fun verifyDistinctUntilChanged() {
        source.set("One")
        assertListEquals(results, listOf("One"))

        source.set("Two")
        assertListEquals(results, listOf("One", "Two"))

        source.set("Two")
        assertListEquals(results, listOf("One", "Two"))

        source.set("Three")
        assertListEquals(results, listOf("One", "Two", "Three"))

        source.set("Three")
        assertListEquals(results, listOf("One", "Two", "Three"))

        source.set("Three")
        assertListEquals(results, listOf("One", "Two", "Three"))

        source.set("Three")
        assertListEquals(results, listOf("One", "Two", "Three"))

        source.set("Two")
        assertListEquals(results, listOf("One", "Two", "Three", "Two"))

        source.set("Two")
        assertListEquals(results, listOf("One", "Two", "Three", "Two"))

        source.set("One")
        assertListEquals(results, listOf("One", "Two", "Three", "Two", "One"))
    }

}