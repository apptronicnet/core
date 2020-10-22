package net.apptronic.core.base.collections

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GeneratedListTest {

    val list = generateList(minValue = -3, maxValue = 5) {
        it.toString()
    }

    @Test
    fun verifyGeneratedList() {
        assertEquals(list.size, 9)
        assertTrue(list.toTypedArray().contentDeepEquals(
                arrayOf("-3", "-2", "-1", "0", "1", "2", "3", "4", "5")
        ))
        assertEquals(list.indexOfZeroValue, 3)
        assertEquals(list.getAtValue(-3), "-3")
        assertEquals(list.getAtValue(0), "0")
        assertEquals(list.getAtValue(5), "5")
        assertEquals(list[0], "-3")
        assertEquals(list[3], "0")
        assertEquals(list[8], "5")
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun indexOutOfBoundsLower() {
        list[-1]
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun indexOutOfBoundsUpper() {
        list[10]
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun valueOutOfBoundsLower() {
        list.getAtValue(-4)
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun valueOutOfBoundsUpper() {
        list.getAtValue(6)
    }

}