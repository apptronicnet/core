package net.apptronic.core.viewmodel.navigation

import net.apptronic.core.base.utils.isInstanceOf
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.function.ofValue
import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.ViewModel
import kotlin.reflect.KClass


/**
 * Class for combining multiple [VisibilityFilter]s for different [ViewModel] types
 */
class VisibilityFilters<VM : IViewModel> : VisibilityFilter<VM> {

    private val filters = mutableListOf<VisibilityFilterWrapper<VM, out VM>>()

    inline fun <reified SubVM : VM> addFilter(filter: VisibilityFilter<SubVM>) {
        addFilter(SubVM::class, filter)
    }

    fun <SubVM : VM> addFilter(clazz: KClass<SubVM>, filter: VisibilityFilter<SubVM>) {
        val wrapper = VisibilityFilterWrapper<VM, SubVM>(clazz, filter)
        filters.add(wrapper)
    }

    override fun isReadyToShow(viewModel: VM): Entity<Boolean> {
        filters.forEach {
            if (it.isFor(viewModel)) {
                return it.isReadyToShow(viewModel)
            }
        }
        return viewModel.ofValue(true)
    }

    @Suppress("UNCHECKED_CAST")
    private class VisibilityFilterWrapper<VM : IViewModel, SubVM : VM>(
            private val type: KClass<SubVM>,
            private val wrapped: VisibilityFilter<SubVM>
    ) : VisibilityFilter<VM> {

        fun isFor(item: Any): Boolean {
            return item isInstanceOf type
        }

        override fun isReadyToShow(viewModel: VM): Entity<Boolean> {
            return wrapped.isReadyToShow(viewModel as SubVM)
        }

    }

}

/**
 * Class for work with [simpleVisibilityFilter]
 */
interface ViewModelWithVisibility {

    /**
     * Return state of [ViewModel] for [VisibilityFilter]
     */
    fun isReadyToShow(): Entity<Boolean>

}

/**
 * Create simple [VisibilityFilter] which tries to case [ViewModel] to [ViewModelWithVisibility] and returns
 * [ViewModelWithVisibility.isReadyToShow] or true if [ViewModel] is not is [ViewModelWithVisibility]
 */
fun <VM : IViewModel> simpleVisibilityFilter(): VisibilityFilter<VM> {
    return SimpleVisibilityFilter()
}

private class SimpleVisibilityFilter<VM : IViewModel> : VisibilityFilter<VM> {

    override fun isReadyToShow(viewModel: VM): Entity<Boolean> {
        return (viewModel as? ViewModelWithVisibility)?.isReadyToShow() ?: viewModel.ofValue(true)
    }

}