package net.apptronic.core.mvvm.viewmodel.navigation

import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.coroutines.coroutineLauncherLocal
import net.apptronic.core.component.entity.base.EntityValue
import net.apptronic.core.component.entity.base.SubjectEntity
import net.apptronic.core.mvvm.viewmodel.ViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModelParent

abstract class Navigator<T>(val parent: ViewModel) : SubjectEntity<T>(), EntityValue<T>, ViewModelParent {

    override val context: Context = parent.context

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