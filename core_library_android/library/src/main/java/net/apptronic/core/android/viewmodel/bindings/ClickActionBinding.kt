package net.apptronic.core.android.viewmodel.bindings

import android.view.View
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.BindingContainer
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.mvvm.viewmodel.ViewModel

fun BindingContainer.bindClickListener(view: View, target: () -> Unit) {
    add(ClickActionBinding(view, target))
}

class ClickActionBinding(
    private val view: View,
    private val target: () -> Unit
) : Binding() {

    override fun onBind(viewModel: ViewModel, viewBinder: ViewBinder<*>) {
        view.setOnClickListener {
            target.invoke()
        }
        onUnbind {
            view.setOnClickListener(null)
        }
    }

}