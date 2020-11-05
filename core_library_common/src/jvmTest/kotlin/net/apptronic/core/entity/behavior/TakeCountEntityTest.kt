package net.apptronic.core.entity.behavior

import net.apptronic.core.assertListEquals
import net.apptronic.core.context.component.Component
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.commons.typedEvent
import net.apptronic.core.testutils.createTestContext
import org.junit.Test

class TakeCountEntityTest {

    val component = Component(createTestContext())
    val source = component.typedEvent<String>()
    val results = mutableListOf<String>()

    fun setResults(entity: Entity<String>) {
        entity.subscribe {
            results.add(it)
        }
    }

    @Test
    fun shouldTakeNone() {
        setResults(source.take(0))
        source.update("One")
        assertListEquals(results, listOf())

        source.update("Two")
        assertListEquals(results, listOf())

        source.update("Three")
        assertListEquals(results, listOf())

        source.update("Four")
        assertListEquals(results, listOf())

        source.update("Five")
        assertListEquals(results, listOf())

        source.update("Six")
        assertListEquals(results, listOf())

        source.update("Seven")
        assertListEquals(results, listOf())
    }

    @Test
    fun shouldTakeOne() {
        setResults(source.takeFirst())
        source.update("One")
        assertListEquals(results, listOf("One"))

        source.update("Two")
        assertListEquals(results, listOf("One"))

        source.update("Three")
        assertListEquals(results, listOf("One"))

        source.update("Four")
        assertListEquals(results, listOf("One"))

        source.update("Five")
        assertListEquals(results, listOf("One"))

        source.update("Six")
        assertListEquals(results, listOf("One"))

        source.update("Seven")
        assertListEquals(results, listOf("One"))
    }

    @Test
    fun shouldTakeFive() {
        setResults(source.take(5))
        source.update("One")
        assertListEquals(results, listOf("One"))

        source.update("Two")
        assertListEquals(results, listOf("One", "Two"))

        source.update("Three")
        assertListEquals(results, listOf("One", "Two", "Three"))

        source.update("Four")
        assertListEquals(results, listOf("One", "Two", "Three", "Four"))

        source.update("Five")
        assertListEquals(results, listOf("One", "Two", "Three", "Four", "Five"))

        source.update("Six")
        assertListEquals(results, listOf("One", "Two", "Three", "Four", "Five"))

        source.update("Seven")
        assertListEquals(results, listOf("One", "Two", "Three", "Four", "Five"))
    }

}