package net.apptronic.core.android.viewmodel.bindings

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.component.entity.entities.Property
import net.apptronic.core.component.entity.subscribe

infix fun EditText.asInputFor(target: Property<String>): InputFieldBinding {
    return InputFieldBinding(this, target)
}

class InputFieldBinding(
    private val view: EditText,
    private val target: Property<String>
) : Binding() {

    override fun onBind() {
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
                target.set(s.toString())
            }
        }
        view.addTextChangedListener(textWatcher)
        onUnbind {
            view.removeTextChangedListener(textWatcher)

        }
    }

}