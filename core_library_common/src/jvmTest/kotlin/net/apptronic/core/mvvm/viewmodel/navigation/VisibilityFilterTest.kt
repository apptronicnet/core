package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.component.assertFalse
import net.apptronic.core.component.assertTrue
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.functions.ofValue
import net.apptronic.core.mvvm.TestViewModel
import net.apptronic.core.mvvm.viewmodel.IViewModel
import org.junit.Test

class VisibilityFilterTest {

    class VM1(val visible: Boolean) : TestViewModel()

    open class VM2(val visible: Boolean) : TestViewModel()
    class VM2x(visible: Boolean) : VM2(visible)

    class VM3(val visible: Boolean) : TestViewModel()

    class Filter1 : VisibilityFilter<VM1> {

        override fun isReadyToShow(viewModel: VM1): Entity<Boolean> {
            return viewModel.ofValue(viewModel.visible)
        }

    }

    class Filter2 : VisibilityFilter<VM2> {

        override fun isReadyToShow(viewModel: VM2): Entity<Boolean> {
            return viewModel.ofValue(viewModel.visible)
        }

    }

    @Test
    fun shouldReturnCorrectValues() {
        val filters = VisibilityFilters<IViewModel>().also {
            it.addFilter(Filter1())
            it.addFilter(Filter2())
        }
        filters.isReadyToShow(VM1(true)).assertTrue()
        filters.isReadyToShow(VM1(false)).assertFalse()
        filters.isReadyToShow(VM2(true)).assertTrue()
        filters.isReadyToShow(VM2(false)).assertFalse()
        filters.isReadyToShow(VM2x(true)).assertTrue()
        filters.isReadyToShow(VM2x(false)).assertFalse()
        filters.isReadyToShow(VM3(true)).assertTrue() // filter not registered, always true
        filters.isReadyToShow(VM3(false)).assertTrue() // filter not registered, always true
    }

}