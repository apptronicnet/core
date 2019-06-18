package net.apptronic.core.component

import net.apptronic.core.base.utils.BaseTestComponent
import net.apptronic.core.component.entity.behavior.doWhen
import net.apptronic.core.component.entity.functions.and
import net.apptronic.core.component.entity.functions.not
import net.apptronic.core.component.entity.functions.or
import net.apptronic.core.component.entity.functions.xor
import org.junit.Test

class BooleanEntityTest {

    private class TestComponent : BaseTestComponent() {

        val left = value<Boolean>()
        val right = value<Boolean>()

        val and = left and right
        val or = left or right
        val xor = left xor right
        val notLeft = left.not()

        val login = value("")
        val password = value("")

    }

    private val component = TestComponent()

    @Test
    fun shouldCallDoWhenOrElse() {

        with(component) {

            var leftFalse = 0
            var leftTrue = 0
            var rightFalse = 0
            var rightTrue = 0

            doWhen(left) {
                leftTrue++
            } orElseDo {
                leftFalse++
            }
            doWhen(right.not()) {
                rightFalse++
            } orElseDo {
                rightTrue++
            }

            assert(leftFalse == 0)
            assert(leftTrue == 0)
            assert(rightFalse == 0)
            assert(rightTrue == 0)

            left.set(true)
            assert(leftTrue == 1)
            assert(leftFalse == 0)

            left.set(false)
            assert(leftTrue == 1)
            assert(leftFalse == 1)

            right.set(true)
            assert(rightTrue == 1)
            assert(rightFalse == 0)

            right.set(false)
            assert(rightTrue == 1)
            assert(rightFalse == 1)

        }

    }

    @Test
    fun shouldSetAs() {

        with(component) {


            left.set(false)
            right.set(false)

            and.assertFalse()
            or.assertFalse()
            xor.assertFalse()
            notLeft.assertTrue()

            left.set(true)
            right.set(false)

            and.assertFalse()
            or.assertTrue()
            xor.assertTrue()
            notLeft.assertFalse()

            left.set(false)
            right.set(true)

            and.assertFalse()
            or.assertTrue()
            xor.assertTrue()
            notLeft.assertTrue()

            left.set(true)
            right.set(true)

            and.assertTrue()
            or.assertTrue()
            xor.assertFalse()
            notLeft.assertFalse()

        }

    }

}