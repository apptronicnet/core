package net.apptronic.common.android.ui.viewmodel.entity

import android.text.Editable
import android.widget.EditText
import android.widget.TextView
import net.apptronic.common.android.ui.utils.BaseTextWatcher
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleHolder

class Property<T>(lifecycleHolder: LifecycleHolder<*>) : SubjectEntity<T>(lifecycleHolder,
        ValueSubject(lifecycleHolder.threadExecutor())) {

    private var value: T? = null

    fun set(value: T?) {
        this.value = value
        onInput(value)
    }

    fun set(property: Property<T>) {
        set(property.get())
    }

    fun get(): T? {
        return value
    }

}

fun Property<*>.textToTextView(textView: TextView) {
    subscribe { textView.text = it.toString() }
}

fun Property<String>.handleInput(view: EditText) {
    view.addTextChangedListener(object : BaseTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            set(s.toString())
        }
    })
}