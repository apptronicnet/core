package net.apptronic.common.core.mvvm

import net.apptronic.common.android.mvvm.components.fragment.FragmentLifecycle
import net.apptronic.common.core.component.Component
import net.apptronic.common.core.component.entity.functions.doWhen
import net.apptronic.common.core.component.entity.functions.variants.*
import net.apptronic.common.core.component.lifecycle.Lifecycle
import org.junit.Test

class BooleanPredicatesTest {

    private val lifecycle = FragmentLifecycle(SynchronousExecutor())

    private class SampleViewModel(lifecycle: Lifecycle) : Component(lifecycle) {

        val left = value<Boolean>()
        val right = value<Boolean>()

        val and = function(left and right)
        val or = function(left or right)
        val xor = function(left xor right)
        val notLeft = function(left.not())

        val login = value("")
        val password = value("")

        val isLoginButtonEnabled = function(
            login.isNoEmpty()
                    and password.isNoEmpty()
                    and password.isTrueThat { length > 8 }
        )

        val loginLength = function(login.map { length })

    }

    private val model = SampleViewModel(lifecycle)

    @Test
    fun shouldCallDoWhenOrElse() {

        with(model) {

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

        with(model) {


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