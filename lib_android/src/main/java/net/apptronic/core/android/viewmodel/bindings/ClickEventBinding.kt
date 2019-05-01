package net.apptronic.core.android.viewmodel.bindings

import android.view.View
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.component.entity.entities.ComponentEvent
import net.apptronic.core.mvvm.viewmodel.ViewModel

infix fun View.sendClicksTo(target: ComponentEvent<Unit>): ClickEventBinding {
    return ClickEventBinding(this, target)
}

class ClickEventBinding(
    private val view: View,
    private val target: ComponentEvent<Unit>
) : Binding() {

    override fun onBind(viewModel: ViewModel, androidView: AndroidView<*>) {
        view.setOnClickListener {
            target.sendEvent(Unit)
        }
        onUnbind {
            view.setOnClickListener(null)
        }
    }

}