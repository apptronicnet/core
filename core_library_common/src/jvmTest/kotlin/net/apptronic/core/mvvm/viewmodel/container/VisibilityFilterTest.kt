package net.apptronic.core.mvvm.viewmodel.container

import com.sun.org.apache.xpath.internal.operations.Bool
import net.apptronic.core.component.assertFalse
import net.apptronic.core.component.assertTrue
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.functions.not
import net.apptronic.core.component.entity.functions.ofValue
import net.apptronic.core.mvvm.TestViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModel
import org.junit.Test
import sun.misc.VM

class VisibilityFilterTest {

    class VM1(val visible: Boolean) : TestViewModel()

    open class VM2(val visible: Boolean) : TestViewModel()
    class VM2x(visible: Boolean) : VM2(visible)

    class VM3(val visible: Boolean) : TestViewModel()

    class Filter1 : VisibilityFilter<VM1> {

        override fun shouldShow(viewModel: VM1): Entity<Boolean> {
            return viewModel.ofValue(viewModel.visible)
        }

    }

    class Filter2 : VisibilityFilter<VM2> {

        override fun shouldShow(viewModel: VM2): Entity<Boolean> {
            return viewModel.ofValue(viewModel.visible)
        }

    }

    @Test
    fun shouldReturnCorrectValues() {
        val filters = VisibilityFilters<ViewModel>().also {
            it.addFilter(Filter1())
            it.addFilter(Filter2())
        }
        filters.shouldShow(VM1(true)).assertTrue()
        filters.shouldShow(VM1(false)).assertFalse()
        filters.shouldShow(VM2(true)).assertTrue()
        filters.shouldShow(VM2(false)).assertFalse()
        filters.shouldShow(VM2x(true)).assertTrue()
        filters.shouldShow(VM2x(false)).assertFalse()
        filters.shouldShow(VM3(true)).assertTrue() // filter not registered, always true
        filters.shouldShow(VM3(false)).assertTrue() // filter not registered, always true
    }

}