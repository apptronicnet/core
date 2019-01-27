package net.apptronic.common.core.mvvm.generic

import android.text.Editable
import android.widget.EditText
import net.apptronic.common.android.mvvm.utils.BaseTextWatcher
import net.apptronic.common.core.mvvm.viewmodel.ViewModel
import net.apptronic.common.core.mvvm.viewmodel.entity.PropertyNotSetException

class TextInputModel(parent: ViewModel) : ViewModel.SubModel(parent) {

    val inputText = value<String>("")
    val textColor = value<Color>()
    val hint = value<Text>()

    fun getText(): String {
        return inputText.getOrNull() ?: ""
    }

}

fun EditText.bind(model: TextInputModel) {
    addTextChangedListener(object : BaseTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            super.afterTextChanged(s)
            val newValue = s.toString()
            try {
                if (model.inputText.get() != newValue) {
                    model.inputText.set(newValue)
                }
            } catch (e: PropertyNotSetException) {
                model.inputText.set(newValue)
            }
        }
    })
    model.inputText.subscribe {
        if (text.toString() != it) {
            setText(it)
        }
    }
    model.textColor.subscribe {
        setTextColor(it.getValue(context))
    }
    model.hint.subscribe {
        setHint(it.getValue(context))
    }
}