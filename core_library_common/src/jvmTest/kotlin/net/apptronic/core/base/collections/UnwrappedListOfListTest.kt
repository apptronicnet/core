package net.apptronic.core.base.collections

import org.junit.Test

class UnwrappedListOfListTest {

    @Test
    fun verifySimpleCombo() {
        val source1 = listOf(1)
        val source2 = listOf(2, 3)
        val source3 = listOf(4, 5, 6)
        val source4 = listOf(7, 8, 9, 10)
        val wrap = wrapLists(source1, source2, source3, source4)
        assert(wrap.size == 10)
        assert(wrap[0] == 1)
        assert(wrap[1] == 2)
        assert(wrap[2] == 3)
        assert(wrap[3] == 4)
        assert(wrap[4] == 5)
        assert(wrap[5] == 6)
        assert(wrap[6] == 7)
        assert(wrap[7] == 8)
        assert(wrap[8] == 9)
        assert(wrap[9] == 10)
    }

    @Test
    fun verifyReverseCombo() {
        val source1 = listOf(1, 2, 3, 4)
        val source2 = listOf(5, 6, 7)
        val source3 = listOf(8, 9)
        val source4 = listOf(10)
        val wrap = wrapLists(source1, source2, source3, source4)
        assert(wrap.size == 10)
        assert(wrap[0] == 1)
        assert(wrap[1] == 2)
        assert(wrap[2] == 3)
        assert(wrap[3] == 4)
        assert(wrap[4] == 5)
        assert(wrap[5] == 6)
        assert(wrap[6] == 7)
        assert(wrap[7] == 8)
        assert(wrap[8] == 9)
        assert(wrap[9] == 10)
    }

    @Test
    fun verifyEmpty() {
        val wrap = wrapLists<Int>()
        assert(wrap.isEmpty())
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun verifyIndexOutOfEmpty1() {
        val wrap = wrapLists<Int>()
        wrap[0]
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun verifyIndexOutOfEmpty2() {
        val wrap = wrapLists<Int>()
        wrap[-1]
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun verifyIndexOutOfEmpty3() {
        val wrap = wrapLists<Int>()
        wrap[100]
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun verifyIndexOutOfFilled1() {
        val source1 = listOf(1)
        val source2 = listOf(2, 3)
        val source3 = listOf(4, 5, 6)
        val source4 = listOf(7, 8, 9, 10)
        val wrap = wrapLists(source1, source2, source3, source4)
        wrap[-1]
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun verifyIndexOutOfFilled2() {
        val source1 = listOf(1)
        val source2 = listOf(2, 3)
        val source3 = listOf(4, 5, 6)
        val source4 = listOf(7, 8, 9, 10)
        val wrap = wrapLists(source1, source2, source3, source4)
        wrap[10]
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun verifyIndexOutOfFilled3() {
        val source1 = listOf(1)
        val source2 = listOf(2, 3)
        val source3 = listOf(4, 5, 6)
        val source4 = listOf(7, 8, 9, 10)
        val wrap = wrapLists(source1, source2, source3, source4)
        wrap[999]
    }

}