package net.apptronic.core.android.viewmodel.bindings

import android.view.View
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.BindingContainer
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.entity.entities.Event
import net.apptronic.core.viewmodel.IViewModel

fun BindingContainer.bindClickListener(view: View, target: Event<Unit>) {
    add(ClickEventBinding(view, target))
}

class ClickEventBinding(
    private val view: View,
    private val target: Event<Unit>
) : Binding() {

    override fun onBind(viewModel: IViewModel, viewBinder: ViewBinder<*>) {
        view.setOnClickListener {
            target.sendEvent(Unit)
        }
        onUnbind {
            view.setOnClickListener(null)
        }
    }

}