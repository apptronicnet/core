package net.apptronic.core.android.viewmodel.bindings

import android.widget.TextView
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.BindingContainer
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.mvvm.viewmodel.IViewModel

fun BindingContainer.bindText(textView: TextView, target: Entity<String>) {
    add(TextBinding(textView, target))
}

class TextBinding(
    private val view: TextView,
    private val target: Entity<String>
) : Binding() {

    override fun onBind(viewModel: IViewModel, viewBinder: ViewBinder<*>) {
        target.subscribe { text ->
            view.text = text
        }
    }

}