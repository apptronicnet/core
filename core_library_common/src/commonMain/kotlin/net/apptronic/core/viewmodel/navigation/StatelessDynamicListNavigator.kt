package net.apptronic.core.viewmodel.navigation

import net.apptronic.core.context.Context
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.onchange.Next
import net.apptronic.core.viewmodel.IViewModel

fun <T : Any, Id : Any, VM : IViewModel> IViewModel.listDynamicNavigator(
        builder: ViewModelBuilder<in T, in Id, in VM>,
        navigatorContext: Context = this.context
): StatelessDynamicListNavigator<T, Id, VM> {
    context.verifyNavigatorContext(navigatorContext)
    return StatelessDynamicListNavigator(this, builder, navigatorContext)
}

fun <T : Any, Id : Any, VM : IViewModel> IViewModel.listDynamicNavigator(
        items: List<T>,
        builder: ViewModelBuilder<in T, in Id, in VM>,
        navigatorContext: Context = this.context
): StatelessDynamicListNavigator<T, Id, VM> {
    val navigator = listDynamicNavigator(builder, navigatorContext)
    navigator.setItems(items)
    return navigator
}

fun <T : Any, Id : Any, VM : IViewModel> IViewModel.listDynamicNavigator(
        source: Entity<out List<T>>,
        builder: ViewModelBuilder<in T, in Id, in VM>,
        navigatorContext: Context = this.context
): StatelessDynamicListNavigator<T, Id, VM> {
    val navigator = listDynamicNavigator(builder, navigatorContext)
    source.subscribe(navigatorContext) {
        navigator.setItems(it)
    }
    return navigator
}

fun <T : Any, Id : Any, VM : IViewModel> IViewModel.listDynamicNavigatorOnChange(
        source: Entity<Next<out List<T>, Any?>>,
        builder: ViewModelBuilder<in T, in Id, in VM>,
        navigatorContext: Context = this.context
): StatelessDynamicListNavigator<T, Id, VM> {
    val navigator = listDynamicNavigator(builder, navigatorContext)
    source.subscribe(navigatorContext) {
        navigator.setItems(it.value, it.change)
    }
    return navigator
}

class StatelessDynamicListNavigator<T : Any, Id : Any, VM : IViewModel>(
        parent: IViewModel,
        builder: ViewModelBuilder<in T, in Id, in VM>,
        navigatorContext: Context
) : DynamicListNavigator<T, Id, VM, Unit>(parent, builder, navigatorContext, Unit) {

    fun setItems(value: List<T>, updateSpec: Any? = null, listDescription: Any? = null) {
        set(value, Unit, updateSpec, listDescription)
    }

}