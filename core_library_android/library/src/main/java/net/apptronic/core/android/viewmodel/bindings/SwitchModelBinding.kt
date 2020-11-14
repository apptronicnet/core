package net.apptronic.core.android.viewmodel.bindings

import android.widget.CompoundButton
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.BindingContainer
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.commons.SwitchModel

fun BindingContainer.bindSwitch(
    button: CompoundButton,
    model: SwitchModel
) {
    +SwitchModelBinding(button, model)
}

private class SwitchModelBinding(
    private val button: CompoundButton,
    private val model: SwitchModel
) : Binding() {

    override fun onBind(viewModel: IViewModel, viewBinder: ViewBinder<*>) {
        model.observeIsEnabledAndFilled().subscribe {
            button.isEnabled = it
        }
        model.subscribe {
            if (button.isChecked != it) {
                button.isChecked = it
            }
        }
        button.setOnCheckedChangeListener { _, isChecked ->
            model.update(isChecked)
        }
    }

}