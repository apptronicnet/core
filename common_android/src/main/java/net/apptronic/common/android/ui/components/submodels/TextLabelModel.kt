package net.apptronic.common.android.ui.components.submodels

import android.widget.TextView
import net.apptronic.common.android.ui.generic.Color
import net.apptronic.common.android.ui.viewmodel.ViewModel

class TextLabelModel(parent: ViewModel) : ViewModel.SubModel(parent) {

    val text = value<String>()
    val textColor = value<Color>()

}

fun TextView.bind(model: TextLabelModel) {
    model.text.subscribe { setText(it) }
    model.textColor.subscribe { setTextColor(it.getValue(context)) }
}