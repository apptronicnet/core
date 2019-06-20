package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.mvvm.viewmodel.ViewModel
import kotlin.reflect.KClass

interface VisibilityFilterableNavigator {

    /**
     * Get current [Navigator] visibility filters
     */
    fun getVisibilityFilters(): VisibilityFilters<ViewModel>

    /**
     * Set use [SimpleVisibilityFilter] for all [ViewModel]s
     */
    fun setSimpleVisibilityFilter() {
        getVisibilityFilters().addFilter(simpleVisibilityFilter<ViewModel>())
    }

    /**
     * Add specific [filter] for [ViewModel] type
     */
    fun <VM : ViewModel> addVisibilityFilter(clazz: KClass<VM>, filter: VisibilityFilter<VM>) {
        getVisibilityFilters().addFilter(clazz, filter)
    }

}

/**
 * Add specific [filter] for [ViewModel] type
 */
inline fun <reified VM : ViewModel> VisibilityFilterableNavigator.addVisibilityFilter(filter: VisibilityFilter<VM>) {
    addVisibilityFilter(VM::class, filter)
}