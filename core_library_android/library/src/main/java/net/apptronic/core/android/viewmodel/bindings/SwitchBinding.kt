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

fun BindingContainer.bindSwitch(button: CompoundButton, value: Value<Boolean>) {
    +SwitchBinding(button, value, value::set)
}

fun BindingContainer.bindSwitch(
    button: CompoundButton,
    source: Entity<Boolean>,
    update: UpdateEntity<Boolean>
) {
    +SwitchBinding(button, source, update::update)
}

fun BindingContainer.bindSwitch(
    button: CompoundButton,
    source: Entity<Boolean>,
    updateCallback: (Boolean) -> Unit
) {
    +SwitchBinding(button, source, updateCallback)
}

private class SwitchBinding(
    private val button: CompoundButton,
    private val source: Entity<Boolean>,
    private val target: (Boolean) -> Unit
) : Binding() {

    private var value: Boolean? = null

    override fun onBind(viewModel: ViewModel, viewBinder: ViewBinder<*>) {
        source.subscribe {
            value = it
            if (button.isChecked != it) {
                button.isChecked = it
            }
        }
        button.setOnCheckedChangeListener { buttonView, isChecked ->
            if (value != isChecked) {
                target(isChecked)
            }
        }
    }

    override fun onUnbind(action: () -> Unit) {
        super.onUnbind(action)
        value = null
    }

}