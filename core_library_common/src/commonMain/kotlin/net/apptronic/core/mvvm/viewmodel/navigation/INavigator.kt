package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.base.EntityValue
import net.apptronic.core.mvvm.viewmodel.IViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModel

class IncorrectContextHierarchyException(msg: String) : IllegalArgumentException(msg)

/**
 * Base interface for all [ViewModel] navigators
 */
interface INavigator<Content> {

    val navigatorContext: Context

    fun <VM : IViewModel> childViewModel(builder: Context.() -> VM): VM {
        return navigatorContext.builder()
    }

    fun IViewModel.verifyContext() {
        val viewModel: IViewModel = this
        if (context.parent != navigatorContext) {
            throw IncorrectContextHierarchyException("$viewModel context should be direct child of navigatorContext")
        }
    }

    val content: EntityValue<Content>

}

fun Context.verifyNavigatorContext(navigatorContext: Context) {
    if (navigatorContext != this && navigatorContext.parent != this) {
        throw IncorrectContextHierarchyException("$navigatorContext context should be same or direct child of parent [ViewModel] context")
    }
}

