package net.apptronic.core.base.collections

import org.junit.Test

class LazyListTest {

    @Test
    fun verifyLazyList1() {
        val result = lazyListOf(listOf(1, 2, 3, 4, 5)) { source, index ->
            source[index].toString()
        }
        assert(result.size == 5)
        assert(result[0] == "1")
        assert(result[1] == "2")
        assert(result[2] == "3")
        assert(result[3] == "4")
        assert(result[4] == "5")
    }

    @Test
    fun verifyLazyList() {
        val result = simpleLazyListOf(listOf(1, 2, 3, 4, 5)) {
            it.toString()
        }
        assert(result.size == 5)
        assert(result[0] == "1")
        assert(result[1] == "2")
        assert(result[2] == "3")
        assert(result[3] == "4")
        assert(result[4] == "5")
    }

}