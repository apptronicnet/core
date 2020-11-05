package net.apptronic.core.android.viewmodel.bindings

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import net.apptronic.core.android.viewmodel.Binding
import net.apptronic.core.android.viewmodel.BindingContainer
import net.apptronic.core.android.viewmodel.ViewBinder
import net.apptronic.core.viewmodel.IViewModel
import net.apptronic.core.viewmodel.commons.TextInputViewModel

fun BindingContainer.bindTextInput(source: EditText, target: TextInputViewModel) {
    add(TextInputBinding(source, target))
}

private class TextInputBinding(
    private val source: EditText,
    private val target: TextInputViewModel
) : Binding(), TextWatcher {

    private val bindingModel = target.getBindingModel()

    override fun onBind(viewModel: IViewModel, viewBinder: ViewBinder<*>) {
        source.isSaveEnabled = false
        bindingModel.observeUpdates().subscribe {
            source.setText(it.text)
            source.setSelection(it.selection.start, it.selection.endInclusive)
        }
        source.addTextChangedListener(this)
        onUnbind {
            source.removeTextChangedListener(this)
            bindingModel.onSelectionChanged(source.selectionStart..source.selectionEnd)
        }
    }

    override fun afterTextChanged(s: Editable) {
        bindingModel.onTextChanged(s.toString())
        bindingModel.onSelectionChanged(source.selectionStart..source.selectionEnd)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // ignore
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // ignore
    }

}