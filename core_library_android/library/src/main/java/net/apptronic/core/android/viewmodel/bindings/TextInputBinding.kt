package net.apptronic.core.android.viewmodel.bindings

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.BindingContainer
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.commons.TextInputModel

fun BindingContainer.bindTextInput(source: EditText, target: TextInputModel) {
    add(TextInputBinding(source, target))
}

private class TextInputBinding(
    private val source: EditText,
    private val target: TextInputModel
) : Binding(), TextWatcher {

    override fun onBind(viewModel: IViewModel, viewBinder: ViewBinder<*>) {
        source.isSaveEnabled = false
        target.subscribe {
            if (source.text.toString() != it) {
                source.setText(it)
            }
        }
        target.selection.subscribe {
            if (source.selectionStart != it.first || source.selectionEnd != it.last) {
                source.setSelection(it.first, it.last)
            }
        }
        source.addTextChangedListener(this)
        onUnbind {
            source.removeTextChangedListener(this)
            target.selection.set(source.selectionStart..source.selectionEnd)
        }
    }

    override fun afterTextChanged(s: Editable) {
        target.update(s.toString())
        target.selection.update(source.selectionStart..source.selectionEnd)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // ignore
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // ignore
    }

}