package net.apptronic.core.android.viewmodel.bindings

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import net.apptronic.core.android.viewmodel.ViewModelBinding
import net.apptronic.core.android.viewmodel.ViewToPredicateBinding
import net.apptronic.core.component.entity.entities.Property
import net.apptronic.core.component.entity.subscribe

class InputFieldBinding : ViewToPredicateBinding<EditText, String, Property<String>> {

    override fun performBinding(
        binding: ViewModelBinding<*>,
        view: EditText,
        target: Property<String>
    ) {
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
        binding.doOnUnbind {
            view.removeTextChangedListener(textWatcher)

        }
    }

}