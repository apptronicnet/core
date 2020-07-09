package net.apptronic.core.android.viewmodel.bindings

import android.widget.CompoundButton
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.BindingContainer
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.base.UpdateEntity
import net.apptronic.core.component.entity.entities.Value
import net.apptronic.core.mvvm.common.SwitchViewModel
import net.apptronic.core.mvvm.viewmodel.ViewModel

fun BindingContainer.bindSwitch(
    button: CompoundButton,
    model: SwitchViewModel
) {
    +SwitchModelBinding(button, model)
}

private class SwitchModelBinding(
    private val button: CompoundButton,
    private val model: SwitchViewModel
) : Binding() {

    override fun onBind(viewModel: ViewModel, viewBinder: ViewBinder<*>) {
        model.observeIsEnabledAndFilled().subscribe {
            button.isEnabled = it
        }
        model.observeState().subscribe {
            if (button.isChecked != it) {
                button.isChecked = it
            }
        }
        button.setOnCheckedChangeListener { _, isChecked ->
            model.updateState(isChecked)
        }
    }

}