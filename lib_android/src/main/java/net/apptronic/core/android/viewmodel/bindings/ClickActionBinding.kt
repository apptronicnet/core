package net.apptronic.core.android.viewmodel.bindings

import android.view.View
import net.apptronic.core.android.viewmodel.Binding

infix fun View.sendClicksTo(target: () -> Unit): ClickActionBinding {
    return ClickActionBinding(this, target)
}

class ClickActionBinding(
    private val view: View,
    private val target: () -> Unit
) : Binding() {

    override fun onBind() {
        view.setOnClickListener {
            target.invoke()
        }
        onUnbind {
            view.setOnClickListener(null)
        }
    }

}