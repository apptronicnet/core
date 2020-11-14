package net.apptronic.core.android.viewmodel.bindings

import android.widget.CompoundButton
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.BindingContainer
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.SubjectEntity
import net.apptronic.core.viewmodel.IViewModel

fun BindingContainer.bindSwitch(button: CompoundButton, value: SubjectEntity<Boolean>) {
    +SwitchBinding(button, value, value::update)
}

fun BindingContainer.bindSwitch(
    button: CompoundButton,
    source: Entity<Boolean>,
    update: SubjectEntity<Boolean>
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

    override fun onBind(viewModel: IViewModel, viewBinder: ViewBinder<*>) {
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