package net.apptronic.core.android.viewmodel.bindings

import android.widget.TextView
import net.apptronic.core.android.viewmodel.AndroidView
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.mvvm.viewmodel.ViewModel

infix fun TextView.setTextFrom(target: Entity<String>): TextBinding {
    return TextBinding(this, target)
}

class TextBinding(
    private val view: TextView,
    private val target: Entity<String>
) : Binding() {

    override fun onBind(viewModel: ViewModel, androidView: AndroidView<*>) {
        target.subscribe { text ->
            view.text = text
        }
    }

}