package net.apptronic.core.android.viewmodel.bindings

import android.widget.CompoundButton
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.BindingContainer
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.component.entity.entities.Value
import net.apptronic.core.mvvm.viewmodel.ViewModel

fun BindingContainer.bindCheckBox(button: CompoundButton, target: Value<Boolean>) {
    +CheckBoxBinding(button, target)
}

class CheckBoxBinding(
    private val button: CompoundButton,
    private val target: Value<Boolean>
) : Binding() {

    override fun onBind(viewModel: ViewModel, viewBinder: ViewBinder<*>) {
        target.subscribe {
            if (button.isChecked != it) {
                button.isChecked = it
            }
        }
        button.setOnCheckedChangeListener { buttonView, isChecked ->
            if (target.getOrNull() != isChecked) {
                target.set(isChecked)
            }
        }
    }

}