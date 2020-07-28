package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.component.context.Context
import net.apptronic.core.mvvm.viewmodel.ViewModel

class IncorrectContextHierarchyException(msg: String) : IllegalArgumentException(msg)

/**
 * Base interface for all [ViewModel] navigators
 */
interface INavigator {

    val navigatorContext: Context

    fun <T : ViewModel> childViewModel(builder: Context.() -> T): T {
        return navigatorContext.builder()
    }

    fun ViewModel.verifyContext() {
        val viewModel: ViewModel = this
        if (context.parent != navigatorContext) {
            throw IncorrectContextHierarchyException("$viewModel context should be direct child of navigatorContext")
        }
    }

}

fun Context.verifyNavigatorContext(navigatorContext: Context) {
    if (navigatorContext != this && navigatorContext.parent != this) {
        throw IncorrectContextHierarchyException("$navigatorContext context should be same or direct child of parent [ViewModel] context")
    }
}

