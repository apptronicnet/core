package net.apptronic.common.android.ui.components.submodels

import android.text.Editable
import android.widget.EditText
import androidx.core.content.ContextCompat
import net.apptronic.common.android.ui.utils.BaseTextWatcher
import net.apptronic.common.android.ui.viewmodel.ViewModel
import net.apptronic.common.android.ui.viewmodel.entity.PropertyNotSetException

class TextInputModel(parent: ViewModel) : ViewModel.SubModel(parent) {

    val text = value<String>("")
    val textColorRes = value<Int>()
    val textColor = value<Int>()
    val hint = value<String>()

    init {
        textColorRes.subscribe {

        }
    }

}

fun TextInputModel.bind(view: EditText) {
    view.addTextChangedListener(object : BaseTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            super.afterTextChanged(s)
            val newValue = s.toString()
            try {
                if (text.get() != newValue) {
                    text.set(newValue)
                }
            } catch (e: PropertyNotSetException) {
                text.set(newValue)
            }
        }
    })
    text.subscribe {
        if (view.text.toString() != it) {
            view.setText(it)
        }
    }
    textColor.subscribe {
        view.setTextColor(ContextCompat.getColor(view.context, it))
    }
}