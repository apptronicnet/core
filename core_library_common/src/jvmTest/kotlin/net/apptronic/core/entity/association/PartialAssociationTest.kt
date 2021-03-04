package net.apptronic.core.entity.association

import net.apptronic.core.BaseContextTest
import net.apptronic.core.entity.commons.value
import net.apptronic.core.record
import org.junit.Test
import kotlin.test.assertEquals

class PartialAssociationTest : BaseContextTest() {

    private data class Point(val x: Int, val y: Int)

    private val coordinates = value<Point>(Point(0, 0))
    private val x = coordinates.associatePart(direct = { it.x }, reverse = { copy(x = it) })
    private val y = coordinates.associatePart(direct = { it.y }, reverse = { copy(y = it) })

    @Test
    fun verifyPartialAssociation() {
        val coordinatesRecord = coordinates.record()
        coordinatesRecord.assertItems(Point(0, 0))
        coordinatesRecord.clear()

        assertEquals(x.get(), 0)
        assertEquals(y.get(), 0)

        val xRecord = x.record()
        xRecord.assertItems(0)
        xRecord.clear()

        val yRecord = y.record()
        yRecord.assertItems(0)
        yRecord.clear()

        coordinates.set(Point(1, 2))

        assertEquals(x.get(), 1)
        assertEquals(y.get(), 2)

        xRecord.assertItems(1)
        xRecord.clear()

        yRecord.assertItems(2)
        yRecord.clear()

        coordinates.set(Point(3, 2))

        assertEquals(x.get(), 3)
        assertEquals(y.get(), 2)

        xRecord.assertItems(3)
        xRecord.clear()

        yRecord.assertItems()

        x.set(10)

        assertEquals(coordinates.get(), Point(10, 2))
        assertEquals(x.get(), 10)
        assertEquals(y.get(), 2)

        xRecord.assertItems(10)
        xRecord.clear()

        yRecord.assertItems()

        y.set(33)

        assertEquals(coordinates.get(), Point(10, 33))
        assertEquals(x.get(), 10)
        assertEquals(y.get(), 33)

        xRecord.assertItems()

        yRecord.assertItems(33)
        yRecord.clear()
    }

}