package net.apptronic.core.android.viewmodel.bindings

import android.view.View
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.BindingContainer
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.mvvm.viewmodel.ViewModel

fun BindingContainer.bindVisibleInvisible(view: View, target: Entity<Boolean>) {
    add(VisibleInvisibleBinding(view, target))
}

class VisibleInvisibleBinding(
    private val view: View,
    private val target: Entity<Boolean>
) : Binding() {

    override fun onBind(viewModel: ViewModel, viewBinder: ViewBinder<*>) {
        target.subscribe { isVisible ->
            view.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
        }
    }

}