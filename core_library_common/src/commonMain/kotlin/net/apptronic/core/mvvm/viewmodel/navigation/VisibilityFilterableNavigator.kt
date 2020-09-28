package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModel
import kotlin.reflect.KClass

interface VisibilityFilterableNavigator {

    /**
     * Get current [Navigator] visibility filters
     */
    fun getVisibilityFilters(): VisibilityFilters<IViewModel>

    /**
     * Set use [SimpleVisibilityFilter] for all [ViewModel]s
     */
    fun setSimpleVisibilityFilter() {
        getVisibilityFilters().addFilter(simpleVisibilityFilter<IViewModel>())
    }

    /**
     * Add specific [filter] for [ViewModel] type
     */
    fun <VM : IViewModel> addVisibilityFilter(clazz: KClass<VM>, filter: VisibilityFilter<VM>) {
        getVisibilityFilters().addFilter(clazz, filter)
    }

}

/**
 * Add specific [filter] for [ViewModel] type
 */
inline fun <reified VM : IViewModel> VisibilityFilterableNavigator.addVisibilityFilter(filter: VisibilityFilter<VM>) {
    addVisibilityFilter(VM::class, filter)
}