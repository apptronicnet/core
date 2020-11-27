package net.apptronic.core.viewmodel.navigation

import net.apptronic.core.context.Context
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.onchange.Next
import net.apptronic.core.viewmodel.IViewModel

fun <T : Any, Id : Any, VM : IViewModel> IViewModel.listDynamicNavigator(
        adapter: ViewModelAdapter<in T, in Id, in VM>,
        navigatorContext: Context = this.context
): StatelessDynamicListNavigator<T, Id, VM> {
    context.verifyNavigatorContext(navigatorContext)
    return StatelessDynamicListNavigator(this, adapter, navigatorContext)
}

fun <T : Any, Id : Any, VM : IViewModel> IViewModel.listDynamicNavigator(
        items: List<T>,
        adapter: ViewModelAdapter<in T, in Id, in VM>,
        navigatorContext: Context = this.context
): StatelessDynamicListNavigator<T, Id, VM> {
    val navigator = listDynamicNavigator(adapter, navigatorContext)
    navigator.setItems(items)
    return navigator
}

fun <T : Any, Id : Any, VM : IViewModel> IViewModel.listDynamicNavigator(
        source: Entity<out List<T>>,
        adapter: ViewModelAdapter<in T, in Id, in VM>,
        navigatorContext: Context = this.context
): StatelessDynamicListNavigator<T, Id, VM> {
    val navigator = listDynamicNavigator(adapter, navigatorContext)
    source.subscribe(navigatorContext) {
        navigator.setItems(it)
    }
    return navigator
}

fun <T : Any, Id : Any, VM : IViewModel> IViewModel.listDynamicNavigatorOnChange(
        source: Entity<Next<out List<T>, Any?>>,
        adapter: ViewModelAdapter<in T, in Id, in VM>,
        navigatorContext: Context = this.context
): StatelessDynamicListNavigator<T, Id, VM> {
    val navigator = listDynamicNavigator(adapter, navigatorContext)
    source.subscribe(navigatorContext) {
        navigator.setItems(it.value, it.change)
    }
    return navigator
}

class StatelessDynamicListNavigator<T : Any, Id : Any, VM : IViewModel>(
        parent: IViewModel,
        adapter: ViewModelAdapter<in T, in Id, in VM>,
        navigatorContext: Context
) : DynamicListNavigator<T, Id, VM, Unit>(parent, adapter, navigatorContext, Unit) {

    fun setItems(value: List<T>, updateSpec: Any? = null, listDescription: Any? = null) {
        set(value, Unit, updateSpec, listDescription)
    }

}