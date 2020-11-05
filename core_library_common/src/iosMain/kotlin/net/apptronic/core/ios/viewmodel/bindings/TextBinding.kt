package net.apptronic.core.ios.viewmodel.bindings

import net.apptronic.core.entity.Entity
import net.apptronic.core.ios.viewmodel.Binding
import net.apptronic.core.ios.viewmodel.BindingContainer
import net.apptronic.core.ios.viewmodel.ViewBinder
import net.apptronic.core.viewmodel.IViewModel
import platform.UIKit.UITextView

fun BindingContainer.bindText(textView: UITextView, target: Entity<String>) {
    add(TextBinding(textView, target))
}

class TextBinding(
        private val view: UITextView,
        private val target: Entity<String>
) : Binding() {

    override fun onBind(viewModel: IViewModel, viewBinder: ViewBinder<*, *>) {
        target.subscribe { text ->
            view.text = text
        }
    }

}