package net.apptronic.core.android.viewmodel.bindings

import android.view.View
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.BindingContainer
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.mvvm.viewmodel.IViewModel

fun BindingContainer.bindEnabledDisabled(view: View, target: Entity<Boolean>) {
    add(EnableDisableBinding(view, target))
}

class EnableDisableBinding(
    private val view: View,
    private val target: Entity<Boolean>
) : Binding() {

    override fun onBind(viewModel: IViewModel, viewBinder: ViewBinder<*>) {
        target.subscribe {
            view.isEnabled = it
        }
    }

}