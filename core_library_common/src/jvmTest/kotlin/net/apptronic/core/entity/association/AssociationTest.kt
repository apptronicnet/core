package net.apptronic.core.entity.association

import net.apptronic.core.BaseContextTest
import net.apptronic.core.entity.commons.mutableValue
import net.apptronic.core.record
import org.junit.Test

class AssociationTest : BaseContextTest() {

    private val source = mutableValue<Int>()
    private val associatedValue = source.associateMutable(direct = { it.toString() }, reverse = { it.toInt() })

    private val sourceRecord = source.record()
    private val sourceUpdatesRecord = source.updates.record()
    private val associatedRecord = associatedValue.record()
    private val associatedUpdatesRecord = associatedValue.updates.record()

    private fun assertState(vararg values: Int) {
        sourceRecord.assertItems(values.toList())
        associatedRecord.assertItems(values.map { it.toString() })
    }

    private fun assertUpdates(vararg values: Int) {
        sourceUpdatesRecord.assertItems(values.toList())
        associatedUpdatesRecord.assertItems(values.map { it.toString() })
    }

    @Test
    fun verifyDirectAssociationWithOnSet() {
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
    fun verifyDirectAssociationWithOnUpdate() {
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
    fun verifyReverseAssociationWithOnSet() {
        assertState()
        assertUpdates()

        associatedValue.set("1")
        assertState(1)
        assertUpdates()

        associatedValue.update("2")
        assertState(1, 2)
        assertUpdates(2)

        associatedValue.set("3")
        assertState(1, 2, 3)
        assertUpdates(2)
    }

    @Test
    fun verifyReverseAssociationWithOnUpdate() {
        assertState()
        assertUpdates()

        associatedValue.update("1")
        assertState(1)
        assertUpdates(1)

        associatedValue.set("2")
        assertState(1, 2)
        assertUpdates(1)

        associatedValue.update("3")
        assertState(1, 2, 3)
        assertUpdates(1, 3)
    }

    @Test
    fun verifyMixedAssociation() {
        assertState()
        assertUpdates()

        associatedValue.set("1")
        assertState(1)
        assertUpdates()

        source.update(2)
        assertState(1, 2)
        assertUpdates(2)

        associatedValue.update("3")
        assertState(1, 2, 3)
        assertUpdates(2, 3)

        source.set(4)
        assertState(1, 2, 3, 4)
        assertUpdates(2, 3)
    }

}