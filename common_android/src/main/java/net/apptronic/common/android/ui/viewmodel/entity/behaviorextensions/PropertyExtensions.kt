package net.apptronic.common.android.ui.viewmodel.entity.behaviorextensions

import android.text.Editable
import android.widget.EditText
import android.widget.TextView
import net.apptronic.common.android.ui.utils.BaseTextWatcher
import net.apptronic.common.android.ui.viewmodel.entity.ViewModelProperty

fun forEachChange(vararg properties: ViewModelProperty<*>, action: () -> Unit) {
    properties.forEach { property ->
        property.subscribe { _ ->
            if (properties.all { it.valueHolder != null }) {
                action()
            }
        }
    }
}

fun <T> ViewModelProperty<T>.assignAsCopyOf(source: ViewModelProperty<T>): ViewModelProperty<T> {
    source.subscribe { set(it) }
    return this
}


fun <T : ViewModelProperty<R>, R, A1> T.assignAsFunctionFrom(
    a1: ViewModelProperty<A1>,
    function: (A1) -> R
): T {
    forEachChange(a1) {
        set(function(a1.get()))
    }
    return this
}

fun <T : ViewModelProperty<R>, R, A1, A2> T.assignAsFunctionFrom(
    a1: ViewModelProperty<A1>,
    a2: ViewModelProperty<A2>,
    function: (A1, A2) -> R
): T {
    forEachChange(a1, a2) {
        set(function(a1.get(), a2.get()))
    }
    return this
}

fun <T : ViewModelProperty<R>, R, A1, A2, A3> T.assignAsFunctionFrom(
    a1: ViewModelProperty<A1>,
    a2: ViewModelProperty<A2>,
    a3: ViewModelProperty<A3>,
    function: (A1, A2, A3) -> R
): T {
    forEachChange(a1, a2, a3) {
        set(function(a1.get(), a2.get(), a3.get()))
    }
    return this
}

fun <T : ViewModelProperty<R>, R, A1, A2, A3, A4> T.assignAsFunctionFrom(
    a1: ViewModelProperty<A1>,
    a2: ViewModelProperty<A2>,
    a3: ViewModelProperty<A3>,
    a4: ViewModelProperty<A4>,
    function: (A1, A2, A3, A4) -> R
): T {
    forEachChange(
        a1,
        a2,
        a3,
        a4
    ) {
        set(function(a1.get(), a2.get(), a3.get(), a4.get()))
    }
    return this
}

fun <T : ViewModelProperty<R>, R, A1, A2, A3, A4, A5> T.assignAsFunctionFrom(
    a1: ViewModelProperty<A1>,
    a2: ViewModelProperty<A2>,
    a3: ViewModelProperty<A3>,
    a4: ViewModelProperty<A4>,
    a5: ViewModelProperty<A5>,
    function: (A1, A2, A3, A4, A5) -> R
): T {
    forEachChange(
        a1,
        a2,
        a3,
        a4,
        a5
    ) {
        set(function(a1.get(), a2.get(), a3.get(), a4.get(), a5.get()))
    }
    return this
}

infix fun <A : ViewModelProperty<*>> TextView.showsTextFrom(property: A) {
    property.subscribe { text = it.toString() }
}

infix fun <A : ViewModelProperty<Int>> TextView.usesTextColorFrom(property: A) {
    property.subscribe { setTextColor(it) }
}

infix fun EditText.savesTextChangesTo(property: ViewModelProperty<String>) {
    property.set(text.toString())
    addTextChangedListener(object : BaseTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            property.set(s.toString())
        }
    })
}