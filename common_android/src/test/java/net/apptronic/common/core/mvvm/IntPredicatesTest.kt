package net.apptronic.common.core.mvvm

import net.apptronic.common.android.mvvm.components.fragment.FragmentLifecycle
import net.apptronic.common.core.mvvm.threading.SynchronousExecutor
import net.apptronic.common.core.mvvm.viewmodel.ViewModel
import net.apptronic.common.core.mvvm.viewmodel.entity.functions.Predicate
import net.apptronic.common.core.mvvm.viewmodel.entity.functions.variants.*
import net.apptronic.common.core.mvvm.viewmodel.lifecycle.Lifecycle
import org.junit.Test

class IntPredicatesTest {

    fun assert(predicate: Predicate<Boolean>) {
        assert(predicate.getPredicateValue())
    }

    private val lifecycle = FragmentLifecycle(SynchronousExecutor())

    private class SampleViewModel(lifecycle: Lifecycle) : ViewModel(lifecycle) {

        val left = value<Int>()
        val right = value<Int>()

        val sum = function(left plus right)
        val subs = function(left minus right)
        val mult = function(left mult right)
        val div = function(left div right)

    }

    private val model = SampleViewModel(lifecycle)

    @Test
    fun shouldCalculate() {
        model.left.set(12)
        model.right.set(5)
        assert(model.sum isEqualsTo 17)
        assert(model.subs isEqualsTo 7)
        assert(model.mult isEqualsTo 60)
        assert(model.div isEqualsTo 2)
    }

}