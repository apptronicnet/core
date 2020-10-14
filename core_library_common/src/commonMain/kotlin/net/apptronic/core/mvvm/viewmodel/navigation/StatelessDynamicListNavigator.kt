package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.onchange.Next
import net.apptronic.core.mvvm.viewmodel.IViewModel

fun <T : Any, Id : Any, VM : IViewModel> IViewModel.listDynamicNavigator(
        builder: ViewModelBuilder<in T, in Id, in VM>, navigatorContext: Context = this.context
): StatelessDynamicListNavigator<T, Id, VM> {
    context.verifyNavigatorContext(navigatorContext)
    return StatelessDynamicListNavigator(this, builder, navigatorContext)
}

fun <T : Any, Id : Any, VM : IViewModel> IViewModel.listDynamicNavigator(
        source: Entity<List<T>>,
        builder: ViewModelBuilder<in T, in Id, in VM>,
        navigatorContext: Context = this.context
): StatelessDynamicListNavigator<T, Id, VM> {
    val navigator = listDynamicNavigator(builder, navigatorContext)
    source.subscribe(navigatorContext) {
        navigator.set(it)
    }
    return navigator
}

fun <T : Any, Id : Any, VM : IViewModel> IViewModel.listDynamicNavigatorOnChange(
        source: Entity<Next<List<T>, Any?>>,
        builder: ViewModelBuilder<in T, in Id, in VM>,
        navigatorContext: Context = this.context
): StatelessDynamicListNavigator<T, Id, VM> {
    val navigator = listDynamicNavigator(builder, navigatorContext)
    source.subscribe(navigatorContext) {
        navigator.set(it.value, it.change)
    }
    return navigator
}

class StatelessDynamicListNavigator<T : Any, Id : Any, VM : IViewModel>(
        parent: IViewModel,
        builder: ViewModelBuilder<in T, in Id, in VM>,
        navigatorContext: Context
) : DynamicListNavigator<T, Id, VM, Unit>(parent, builder, navigatorContext, Unit) {

    fun set(value: List<T>, changeInfo: Any? = null, listDescription: Any? = null) {
        set(value, Unit, changeInfo, listDescription)
    }

}