package net.apptronic.core.component

import net.apptronic.core.component.entity.behavior.mergeArray
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.component.extensions.BaseComponent
import net.apptronic.core.testutils.testContext
import org.junit.Test

class MergeArrayTest {

    class TestComponent : BaseComponent(testContext()) {

        val val1 = value(1)
        val val2 = value(2)
        val val3 = value(3)
        val val4 = value(4)

        init {
            mergeArray(val1, val2, val3, val4) { values ->
                values.filter { it < 2 }
            }.subscribe {
                lessThan2 = it
            }
            mergeArray(val1, val2, val3, val4) { values ->
                values.filter { it > 2 }
            }.subscribe {
                moreThan2 = it
            }
        }

        lateinit var lessThan2: List<Int>
        lateinit var moreThan2: List<Int>

    }

    private val component = TestComponent()

    @Test
    fun verify() {
        assert(component.lessThan2.toTypedArray().contentEquals(arrayOf(1)))
        assert(component.moreThan2.toTypedArray().contentEquals(arrayOf(3, 4)))
    }


}