package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.coroutines.coroutineLauncherContextual
import net.apptronic.core.component.entity.EntityValue
import net.apptronic.core.component.entity.entities.ComponentEntity
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelParent

abstract class Navigator<T>(viewModel: ViewModel) : ComponentEntity<T>(viewModel.context),
        EntityValue<T>, ViewModelParent {

    protected val coroutineLauncher = context.coroutineLauncherContextual()

    override fun getValueHolder(): ValueHolder<T>? {
        return ValueHolder(get())
    }

    override fun isSet(): Boolean {
        return true
    }

    override fun doIfSet(action: (T) -> Unit): Boolean {
        action.invoke(get())
        return true
    }

}