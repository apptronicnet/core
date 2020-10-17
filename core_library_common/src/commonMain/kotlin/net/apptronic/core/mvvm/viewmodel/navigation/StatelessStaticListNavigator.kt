package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.onchange.Next
import net.apptronic.core.mvvm.viewmodel.IViewModel

fun IViewModel.listNavigator(navigatorContext: Context = this.context): StatelessStaticListNavigator {
    context.verifyNavigatorContext(navigatorContext)
    return StatelessStaticListNavigator(this, navigatorContext)
}

fun <T, Id, VM : IViewModel> IViewModel.listNavigator(
        source: Entity<out List<T>>,
        builder: ViewModelBuilder<T, Id, VM>,
        navigatorContext: Context = this.context
): StatelessStaticListNavigator {
    val listBuilder = listBuilder(builder)
    listBuilder.updateFrom(source.switchContext(navigatorContext))
    val listNavigator = listNavigator(navigatorContext)
    listBuilder.subscribe {
        listNavigator.set(it.value, it.change)
    }
    return listNavigator(navigatorContext)
}

fun <T, Id, VM : IViewModel> IViewModel.listNavigatorOnChange(
        source: Entity<Next<out List<T>, out Any>>,
        builder: ViewModelBuilder<T, Id, VM>,
        navigatorContext: Context = this.context
): StatelessStaticListNavigator {
    val listBuilder = listBuilder(builder)
    val listNavigator = listNavigator(navigatorContext)
    listBuilder.updateFromChanges(source.switchContext(navigatorContext))
    listBuilder.subscribe {
        listNavigator.set(it.value, it.change)
    }
    return listNavigator(navigatorContext)
}

fun IViewModel.listNavigator(source: Entity<List<IViewModel>>, navigatorContext: Context = this.context): StatelessStaticListNavigator {
    context.verifyNavigatorContext(navigatorContext)
    return StatelessStaticListNavigator(this, navigatorContext).apply {
        source.subscribe(context) {
            set(it)
        }
    }
}

fun IViewModel.listNavigatorOnChange(source: Entity<Next<List<IViewModel>, Any>>, navigatorContext: Context = this.context): StatelessStaticListNavigator {
    context.verifyNavigatorContext(navigatorContext)
    return StatelessStaticListNavigator(this, navigatorContext).apply {
        source.subscribe(context) {
            set(it.value, it.change)
        }
    }
}

class StatelessStaticListNavigator(
        parent: IViewModel, navigatorContext: Context
) : StaticListNavigator<Unit>(parent, navigatorContext, Unit) {

    override fun requestCloseSelf(viewModel: IViewModel, transitionInfo: Any?) {
        update(transitionInfo) {
            it.remove(viewModel)
        }
    }

    override fun calculateFilteredState(all: List<IViewModel>, visible: List<IViewModel>, state: Unit) = Unit

    fun update(updateSpec: Any? = null, action: (MutableList<IViewModel>) -> Unit) {
        update(Unit, updateSpec, action)
    }

    fun set(list: List<IViewModel>, updateSpec: Any? = null) {
        set(list, Unit, updateSpec)
    }

}