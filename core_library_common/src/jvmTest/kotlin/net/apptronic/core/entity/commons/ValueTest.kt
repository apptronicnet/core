package net.apptronic.core.entity.commons

import net.apptronic.core.BaseContextTest
import net.apptronic.core.record
import org.junit.Test

class ValueTest : BaseContextTest() {

    private val source = mutableValue<String>()
    private val results = source.record()
    private val updates = source.updates.record()

    @Test
    fun verifyDistinctUntilChanged() {
        source.set("One")
        results.assertItems("One")
        updates.assertItems()

        source.set("Two")
        results.assertItems("One", "Two")
        updates.assertItems()

        source.set("Two")
        results.assertItems("One", "Two")
        updates.assertItems()

        source.update("Three")
        results.assertItems("One", "Two", "Three")
        updates.assertItems("Three")

        source.set("Three")
        results.assertItems("One", "Two", "Three")
        updates.assertItems("Three")

        source.update("Three")
        results.assertItems("One", "Two", "Three")
        updates.assertItems("Three")

        source.set("Three")
        results.assertItems("One", "Two", "Three")
        updates.assertItems("Three")

        source.set("Two")
        results.assertItems("One", "Two", "Three", "Two")
        updates.assertItems("Three")

        source.update("Two")
        results.assertItems("One", "Two", "Three", "Two")
        updates.assertItems("Three")

        source.update("One")
        results.assertItems("One", "Two", "Three", "Two", "One")
        updates.assertItems("Three", "One")
    }

}