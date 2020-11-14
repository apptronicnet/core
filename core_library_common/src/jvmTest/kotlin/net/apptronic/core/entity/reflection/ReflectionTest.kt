package net.apptronic.core.entity.reflection

import net.apptronic.core.BaseContextTest
import net.apptronic.core.entity.commons.mutableValue
import net.apptronic.core.record
import org.junit.Test

class ReflectionTest : BaseContextTest() {

    private val source = mutableValue<Int>()
    private val reflection = source.reflect(direct = { it.toString() }, reverse = { it.toInt() })

    private val sourceRecord = source.record()
    private val sourceUpdatesRecord = source.updates.record()
    private val reflectionRecord = reflection.record()
    private val reflectionUpdatesRecord = reflection.updates.record()

    private fun assertState(vararg values: Int) {
        sourceRecord.assertItems(values.toList())
        reflectionRecord.assertItems(values.map { it.toString() })
    }

    private fun assertUpdates(vararg values: Int) {
        sourceUpdatesRecord.assertItems(values.toList())
        reflectionUpdatesRecord.assertItems(values.map { it.toString() })
    }

    @Test
    fun verifyDirectReflectionWithOnSet() {
        assertState()
        assertUpdates()

        source.set(1)
        assertState(1)
        assertUpdates()

        source.update(2)
        assertState(1, 2)
        assertUpdates(2)

        source.set(3)
        assertState(1, 2, 3)
        assertUpdates(2)
    }

    @Test
    fun verifyDirectReflectionWithOnUpdate() {
        assertState()
        assertUpdates()

        source.update(1)
        assertState(1)
        assertUpdates(1)

        source.set(2)
        assertState(1, 2)
        assertUpdates(1)

        source.update(3)
        assertState(1, 2, 3)
        assertUpdates(1, 3)
    }

    @Test
    fun verifyReverseReflectionWithOnSet() {
        assertState()
        assertUpdates()

        reflection.set("1")
        assertState(1)
        assertUpdates()

        reflection.update("2")
        assertState(1, 2)
        assertUpdates(2)

        reflection.set("3")
        assertState(1, 2, 3)
        assertUpdates(2)
    }

    @Test
    fun verifyReverseReflectionWithOnUpdate() {
        assertState()
        assertUpdates()

        reflection.update("1")
        assertState(1)
        assertUpdates(1)

        reflection.set("2")
        assertState(1, 2)
        assertUpdates(1)

        reflection.update("3")
        assertState(1, 2, 3)
        assertUpdates(1, 3)
    }

    @Test
    fun verifyMixedReflection() {
        assertState()
        assertUpdates()

        reflection.set("1")
        assertState(1)
        assertUpdates()

        source.update(2)
        assertState(1, 2)
        assertUpdates(2)

        reflection.update("3")
        assertState(1, 2, 3)
        assertUpdates(2, 3)

        source.set(4)
        assertState(1, 2, 3, 4)
        assertUpdates(2, 3)
    }

}