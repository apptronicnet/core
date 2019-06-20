package net.apptronic.core.mvvm.viewmodel.container

import net.apptronic.core.base.utils.isInstanceOf
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.base.ConstantEntity
import net.apptronic.core.component.entity.functions.ofValue
import net.apptronic.core.mvvm.viewmodel.ViewModel
import kotlin.reflect.KClass

interface VisibilityFilter<VM : ViewModel> {

    fun shouldShow(viewModel: VM):  Entity<Boolean> {
        return viewModel.ofValue(true)
    }

}

class VisibilityFilters<VM : ViewModel> : VisibilityFilter<VM> {

    private val filters = mutableListOf<VisibilityFilterWrapper<VM, out VM>>()

    inline fun <reified SubVM : VM> addFilter(filter: VisibilityFilter<SubVM>) {
        addFilter(SubVM::class, filter)
    }

    fun <SubVM : VM> addFilter(clazz: KClass<SubVM>, filter: VisibilityFilter<SubVM>) {
        val wrapper = VisibilityFilterWrapper<VM, SubVM>(clazz, filter)
        filters.add(wrapper)
    }

    override fun shouldShow(viewModel: VM): Entity<Boolean> {
        filters.forEach {
            if (it.isFor(viewModel)) {
                return it.shouldShow(viewModel)
            }
        }
        return viewModel.ofValue(true)
    }

    private class VisibilityFilterWrapper<VM : ViewModel, SubVM : VM>(
            private val type: KClass<SubVM>,
            private val wrapped: VisibilityFilter<SubVM>
    ) : VisibilityFilter<VM> {

        fun isFor(item: Any): Boolean {
            return item isInstanceOf type
        }

        override fun shouldShow(viewModel: VM): Entity<Boolean> {
            return wrapped.shouldShow(viewModel as SubVM)
        }

    }

}