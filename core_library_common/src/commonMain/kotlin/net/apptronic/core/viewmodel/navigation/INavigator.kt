package net.apptronic.core.viewmodel.navigation

import net.apptronic.core.context.Context
import net.apptronic.core.entity.base.Property
import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.ViewModel

class IncorrectContextHierarchyException(msg: String) : IllegalArgumentException(msg)

/**
 * Base interface for all [ViewModel] navigators
 */
interface INavigator<Content> {

    val parentContext: Context

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

    val content: Property<Content>

}

fun Context.verifyNavigatorContext(navigatorContext: Context) {
    if (navigatorContext != this && navigatorContext.parent != this) {
        throw IncorrectContextHierarchyException("$navigatorContext context should be same or direct child of parent [ViewModel] context")
    }
}

