package net.apptronic.core.entity.behavior

import net.apptronic.core.context.component.terminate
import net.apptronic.core.entity.mutableValue
import net.apptronic.core.entity.value
import net.apptronic.core.testutils.BaseTestComponent
import org.junit.Test

class ValueWatcherTestCase : BaseTestComponent() {

    @Test
    fun verifyValueFlow() {
        val value = value<Int>()

        val eachNew = mutableListOf<Int>()
        val eachReplaced = mutableListOf<Int>()
        val eachRecycled = mutableListOf<Int>()
        value.watch().forEachValue {
            eachNew.add(it)
        }
        value.watch().forEachReplacedValue {
            eachReplaced.add(it)
        }
        value.watch().forEachRecycledValue {
            eachRecycled.add(it)
        }
        assert(eachNew.toTypedArray().contentDeepEquals(arrayOf()))
        assert(eachReplaced.toTypedArray().contentDeepEquals(arrayOf()))
        assert(eachRecycled.toTypedArray().contentDeepEquals(arrayOf()))

        value.set(1)

        assert(eachNew.toTypedArray().contentDeepEquals(arrayOf(1)))
        assert(eachReplaced.toTypedArray().contentDeepEquals(arrayOf()))
        assert(eachRecycled.toTypedArray().contentDeepEquals(arrayOf()))

        value.set(2)

        assert(eachNew.toTypedArray().contentDeepEquals(arrayOf(1, 2)))
        assert(eachReplaced.toTypedArray().contentDeepEquals(arrayOf(1)))
        assert(eachRecycled.toTypedArray().contentDeepEquals(arrayOf(1)))

        value.set(3)

        assert(eachNew.toTypedArray().contentDeepEquals(arrayOf(1, 2, 3)))
        assert(eachReplaced.toTypedArray().contentDeepEquals(arrayOf(1, 2)))
        assert(eachRecycled.toTypedArray().contentDeepEquals(arrayOf(1, 2)))

        terminate()

        assert(eachNew.toTypedArray().contentDeepEquals(arrayOf(1, 2, 3)))
        assert(eachReplaced.toTypedArray().contentDeepEquals(arrayOf(1, 2)))
        assert(eachRecycled.toTypedArray().contentDeepEquals(arrayOf(1, 2, 3)))
    }

    @Test
    fun verifyValueWithInitialSetFlow() {
        val value = value<Int>(0)

        val eachNew = mutableListOf<Int>()
        val eachReplaced = mutableListOf<Int>()
        val eachRecycled = mutableListOf<Int>()
        value.watch().forEachValue {
            eachNew.add(it)
        }
        value.watch().forEachReplacedValue {
            eachReplaced.add(it)
        }
        value.watch().forEachRecycledValue {
            eachRecycled.add(it)
        }
        assert(eachNew.toTypedArray().contentDeepEquals(arrayOf(0)))
        assert(eachReplaced.toTypedArray().contentDeepEquals(arrayOf()))
        assert(eachRecycled.toTypedArray().contentDeepEquals(arrayOf()))

        value.set(1)

        assert(eachNew.toTypedArray().contentDeepEquals(arrayOf(0, 1)))
        assert(eachReplaced.toTypedArray().contentDeepEquals(arrayOf(0)))
        assert(eachRecycled.toTypedArray().contentDeepEquals(arrayOf(0)))

        value.set(2)

        assert(eachNew.toTypedArray().contentDeepEquals(arrayOf(0, 1, 2)))
        assert(eachReplaced.toTypedArray().contentDeepEquals(arrayOf(0, 1)))
        assert(eachRecycled.toTypedArray().contentDeepEquals(arrayOf(0, 1)))

        value.set(3)

        assert(eachNew.toTypedArray().contentDeepEquals(arrayOf(0, 1, 2, 3)))
        assert(eachReplaced.toTypedArray().contentDeepEquals(arrayOf(0, 1, 2)))
        assert(eachRecycled.toTypedArray().contentDeepEquals(arrayOf(0, 1, 2)))

        terminate()

        assert(eachNew.toTypedArray().contentDeepEquals(arrayOf(0, 1, 2, 3)))
        assert(eachReplaced.toTypedArray().contentDeepEquals(arrayOf(0, 1, 2)))
        assert(eachRecycled.toTypedArray().contentDeepEquals(arrayOf(0, 1, 2, 3)))
    }

    @Test
    fun verifySameReferenceNotTriggered() {
        val any1 = Any()
        val any2 = Any()
        val any3 = Any()
        val value = mutableValue<Any>(any1)

        val each = mutableListOf<Any>()
        val eachNew = mutableListOf<Any>()
        val eachReplaced = mutableListOf<Any>()
        val eachRecycled = mutableListOf<Any>()
        value.watch().forEachValue {
            each.add(it)
        }
        value.watch().forEachNewValue {
            eachNew.add(it)
        }
        value.watch().forEachReplacedValue {
            eachReplaced.add(it)
        }
        value.watch().forEachRecycledValue {
            eachRecycled.add(it)
        }
        assert(each.toTypedArray().contentDeepEquals(arrayOf(any1)))
        assert(eachNew.toTypedArray().contentDeepEquals(arrayOf()))
        assert(eachReplaced.toTypedArray().contentDeepEquals(arrayOf()))
        assert(eachRecycled.toTypedArray().contentDeepEquals(arrayOf()))

        value.set(any2)
        assert(each.toTypedArray().contentDeepEquals(arrayOf(any1, any2)))
        assert(eachNew.toTypedArray().contentDeepEquals(arrayOf(any2)))
        assert(eachReplaced.toTypedArray().contentDeepEquals(arrayOf(any1)))
        assert(eachRecycled.toTypedArray().contentDeepEquals(arrayOf(any1)))

        value.set(any2)
        assert(each.toTypedArray().contentDeepEquals(arrayOf(any1, any2)))
        assert(eachReplaced.toTypedArray().contentDeepEquals(arrayOf(any1)))
        assert(eachRecycled.toTypedArray().contentDeepEquals(arrayOf(any1)))

        value.set(any3)
        assert(each.toTypedArray().contentDeepEquals(arrayOf(any1, any2, any3)))
        assert(eachNew.toTypedArray().contentDeepEquals(arrayOf(any2, any3)))
        assert(eachReplaced.toTypedArray().contentDeepEquals(arrayOf(any1, any2)))
        assert(eachRecycled.toTypedArray().contentDeepEquals(arrayOf(any1, any2)))

        terminate()

        assert(each.toTypedArray().contentDeepEquals(arrayOf(any1, any2, any3)))
        assert(eachNew.toTypedArray().contentDeepEquals(arrayOf(any2, any3)))
        assert(eachReplaced.toTypedArray().contentDeepEquals(arrayOf(any1, any2)))
        assert(eachRecycled.toTypedArray().contentDeepEquals(arrayOf(any1, any2, any3)))
    }

}