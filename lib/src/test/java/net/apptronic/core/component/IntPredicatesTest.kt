package net.apptronic.core.component

import net.apptronic.common.utils.BaseTestComponent
import net.apptronic.core.component.entity.functions.variants.*
import org.junit.Test

class IntPredicatesTest {

    private class TestComponent : BaseTestComponent() {

        val left = value<Int>()
        val right = value<Int>()

        val sum = left plus right
        val subs = left minus right
        val mult = left mult right
        val div = left div right

    }

    private val component = TestComponent()

    @Test
    fun shouldCalculate() {
        component.left.set(12)
        component.right.set(5)
        assert(component.sum isEqualsTo 17)
        assert(component.subs isEqualsTo 7)
        assert(component.mult isEqualsTo 60)
        assert(component.div isEqualsTo 2)
    }

}