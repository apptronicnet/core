package net.apptronic.core.utils

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EqComparatorTest {

    private class Value(val s: String)

    private class ValueComparator : EqComparator<Value> {

        override fun isEquals(left: Value, right: Value): Boolean {
            return left.s == right.s
        }

    }

    private val valueComparator = ValueComparator()
    private val referenceComparator = ReferenceEqComparator<Value>()

    @Test
    fun verifySimpleComparator() {
        assertTrue(valueComparator.isEquals(Value("1"), Value("1")))
        assertTrue(valueComparator.isEquals(Value("123"), Value("123")))
        assertTrue(valueComparator.isEquals(Value("Some"), Value("Some")))
        assertTrue(valueComparator.isEquals(Value("Another"), Value("Another")))
        assertTrue(valueComparator.isEqualsNullable(null, null))

        assertFalse(valueComparator.isEquals(Value("2"), Value("1")))
        assertFalse(valueComparator.isEquals(Value("123"), Value("321")))
        assertFalse(valueComparator.isEquals(Value("Some"), Value("Another")))
        assertFalse(valueComparator.isEquals(Value("Another"), Value("Some")))
        assertFalse(valueComparator.isEqualsNullable(Value("Some"), null))
        assertFalse(valueComparator.isEqualsNullable(null, Value("Another")))
    }

    @Test
    fun verifyReferenceEqComparator() {
        assertFalse(referenceComparator.isEquals(Value("1"), Value("1")))
        assertFalse(referenceComparator.isEquals(Value("123"), Value("123")))
        assertFalse(referenceComparator.isEquals(Value("Some"), Value("Some")))
        assertFalse(referenceComparator.isEquals(Value("Another"), Value("Another")))
        assertTrue(referenceComparator.isEqualsNullable(null, null))

        val value = Value("1")
        assertTrue(referenceComparator.isEqualsNullable(value, value))
    }

    @Test
    fun verifyListComparator() {
        val listComparator = ListEqComparator(valueComparator)
        assertTrue(
                listComparator.isEquals(
                        listOf(),
                        listOf()
                )
        )
        assertTrue(
                listComparator.isEquals(
                        listOf(Value("One"), Value("Two"), Value("Three")),
                        listOf(Value("One"), Value("Two"), Value("Three"))
                )
        )
        assertFalse(
                listComparator.isEquals(
                        listOf(),
                        listOf(Value("One"), Value("Two"), Value("Three"))
                )
        )
        assertFalse(
                listComparator.isEquals(
                        listOf(Value("One"), Value("Two"), Value("Three")),
                        listOf()
                )
        )
        assertFalse(
                listComparator.isEquals(
                        listOf(Value("One"), Value("Two"), Value("Three")),
                        listOf(Value("Three"), Value("Two"), Value("One"))
                )
        )
        assertFalse(
                listComparator.isEquals(
                        listOf(Value("One")),
                        listOf(Value("Two"))
                )
        )
        assertFalse(
                listComparator.isEquals(
                        listOf(Value("One"), Value("Two")),
                        listOf(Value("One"))
                )
        )
        assertFalse(
                listComparator.isEquals(
                        listOf(Value("One")),
                        listOf(Value("One"), Value("Two"))
                )
        )
    }

    @Test
    fun verifySetComparator() {
        val setComparator = SetEqComparator(valueComparator)
        assertTrue(
                setComparator.isEquals(
                        setOf(),
                        setOf()
                )
        )
        assertTrue(
                setComparator.isEquals(
                        setOf(Value("One"), Value("Two"), Value("Three")),
                        setOf(Value("One"), Value("Two"), Value("Three"))
                )
        )
        assertTrue(
                setComparator.isEquals(
                        setOf(Value("One"), Value("Two"), Value("Three")),
                        setOf(Value("Three"), Value("Two"), Value("One"))
                )
        )
        assertFalse(
                setComparator.isEquals(
                        setOf(),
                        setOf(Value("One"), Value("Two"), Value("Three"))
                )
        )
        assertFalse(
                setComparator.isEquals(
                        setOf(Value("One"), Value("Two"), Value("Three")),
                        setOf()
                )
        )
        assertFalse(
                setComparator.isEquals(
                        setOf(Value("One")),
                        setOf(Value("Two"))
                )
        )
        assertFalse(
                setComparator.isEquals(
                        setOf(Value("One"), Value("Two")),
                        setOf(Value("One"))
                )
        )
        assertFalse(
                setComparator.isEquals(
                        setOf(Value("One")),
                        setOf(Value("One"), Value("Two"))
                )
        )
    }

    @Test
    fun verifyMapComparator() {
        val setComparator = MapEqComparator<Int, Value>(valueComparator)
        assertTrue(
                setComparator.isEquals(
                        mapOf(),
                        mapOf()
                )
        )
        assertTrue(
                setComparator.isEquals(
                        mapOf(1 to Value("One"), 2 to Value("Two"), 3 to Value("Three")),
                        mapOf(1 to Value("One"), 2 to Value("Two"), 3 to Value("Three"))
                )
        )
        assertTrue(
                setComparator.isEquals(
                        mapOf(1 to Value("One"), 2 to Value("Two"), 3 to Value("Three")),
                        mapOf(3 to Value("Three"), 2 to Value("Two"), 1 to Value("One"))
                )
        )
        assertFalse(
                setComparator.isEquals(
                        mapOf(),
                        mapOf(1 to Value("One"), 2 to Value("Two"), 3 to Value("Three"))
                )
        )
        assertFalse(
                setComparator.isEquals(
                        mapOf(1 to Value("One"), 2 to Value("Two"), 3 to Value("Three")),
                        mapOf()
                )
        )
        assertFalse(
                setComparator.isEquals(
                        mapOf(1 to Value("One"), 2 to Value("Two"), 3 to Value("Three")),
                        mapOf(1 to Value("One"), 2 to Value("Two"))
                )
        )
        assertFalse(
                setComparator.isEquals(
                        mapOf(1 to Value("One"), 2 to Value("NotTwo"), 3 to Value("Three")),
                        mapOf(1 to Value("One"), 2 to Value("Two"), 3 to Value("Three"))
                )
        )
        assertFalse(
                setComparator.isEquals(
                        mapOf(1 to Value("One")),
                        mapOf(1 to Value("NotOne"))
                )
        )
        assertFalse(
                setComparator.isEquals(
                        mapOf(1 to Value("One"), 2 to Value("Two")),
                        mapOf(1 to Value("Two"), 2 to Value("One"))
                )
        )
        assertFalse(
                setComparator.isEquals(
                        mapOf(1 to Value("One"), 2 to Value("Two")),
                        mapOf(2 to Value("One"), 1 to Value("Two"))
                )
        )
    }

}