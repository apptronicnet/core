package net.apptronic.common.core.mvvm.generic

import android.widget.TextView
import net.apptronic.common.core.mvvm.viewmodel.ViewModel

class TextLabelModel(parent: ViewModel) : ViewModel.SubModel(parent) {

    val text = value<String>()
    val textColor = value<Color>()

}

fun TextView.bind(model: TextLabelModel) {
    model.text.subscribe { setText(it) }
    model.textColor.subscribe { setTextColor(it.getValue(context)) }
}