package net.apptronic.common.android.ui.viewmodel.entity

import android.text.Editable
import android.widget.EditText
import android.widget.TextView
import net.apptronic.common.android.ui.utils.BaseTextWatcher
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleHolder

abstract class Property<T>(lifecycleHolder: LifecycleHolder<*>) : SubjectEntity<T>(lifecycleHolder,
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

class ViewProperty<T>(lifecycleHolder: LifecycleHolder<*>) : Property<T>(lifecycleHolder)

class StateProperty<T>(lifecycleHolder: LifecycleHolder<*>) : Property<T>(lifecycleHolder)

infix fun <A : ViewProperty<*>> TextView.setTextFrom(property: A) {
    property.subscribe { text = it.toString() }
}

infix fun EditText.saveChangesTo(property: StateProperty<String>) {
    property.set(text.toString())
    addTextChangedListener(object : BaseTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            property.set(s.toString())
        }
    })
}