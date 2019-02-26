package net.apptronic.common.core.component

import net.apptronic.common.core.component.entity.functions.variants.*
import net.apptronic.common.utils.BaseTestComponent
import org.junit.Test

class IntPredicatesTest {

    private class TestComponent : BaseTestComponent() {

        val left = value<Int>()
        val right = value<Int>()

        val sum = function(left plus right)
        val subs = function(left minus right)
        val mult = function(left mult right)
        val div = function(left div right)

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