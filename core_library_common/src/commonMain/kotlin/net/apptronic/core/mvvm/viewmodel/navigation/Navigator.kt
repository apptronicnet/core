package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.coroutines.coroutineLauncherLocal
import net.apptronic.core.component.entity.EntityValue
import net.apptronic.core.component.entity.entities.ComponentEntity
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelParent

abstract class Navigator<T>(val parent: ViewModel) : ComponentEntity<T>(parent.context),
        EntityValue<T>, ViewModelParent {

    protected val coroutineLauncher = context.coroutineLauncherLocal()

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