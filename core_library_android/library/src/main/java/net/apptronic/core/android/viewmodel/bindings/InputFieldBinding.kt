package net.apptronic.core.android.viewmodel.bindings

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.BindingContainer
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.component.entity.entities.Value
import net.apptronic.core.mvvm.viewmodel.ViewModel

fun BindingContainer.bindTextInput(editText: EditText, target: Value<String>) {
    add(InputFieldBinding(editText, target))
}

class InputFieldBinding(
    private val view: EditText,
    private val target: Value<String>
) : Binding() {

    override fun onBind(viewModel: ViewModel, viewBinder: ViewBinder<*>) {
        target.subscribe {
            if (it != view.text.toString()) {
                view.setText(it)
                view.setSelection(it.length)
            }
        }
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                target.update(s.toString())
            }
        }
        view.addTextChangedListener(textWatcher)
        onUnbind {
            view.removeTextChangedListener(textWatcher)

        }
    }

}