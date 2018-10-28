package net.apptronic.common.android.ui.viewmodel.entity

import android.text.Editable
import android.widget.EditText
import android.widget.TextView
import net.apptronic.common.android.ui.utils.BaseTextWatcher
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleHolder

class Property<T>(lifecycleHolder: LifecycleHolder<*>) : SubjectEntity<T>(lifecycleHolder,
        ValueSubject(lifecycleHolder.threadExecutor())) {

    internal var valueHolder: ValueHolder<T>? = null

    fun set(value: T) {
        this.valueHolder = ValueHolder(value)
        onInput(value)
    }

    fun set(property: Property<T>) {
        property.valueHolder?.let {
            set(it.value)
        }
    }

    fun get(): T {
        return valueHolder?.value ?: throw IllegalStateException("No value set")
    }

}

private fun forEachChange(vararg properties: Property<*>, action: () -> Unit) {
    properties.forEach { property ->
        property.subject.subscribe { _ ->
            if (properties.all { it.valueHolder != null }) {
                action()
            }
        }
    }
}

fun <T : Property<R>, R, A1> T.asFunctionFrom(a1: Property<A1>,
                                              function: (A1) -> R): T {
    forEachChange(a1) {
        set(function(a1.get()))
    }
    return this
}

fun <T : Property<R>, R, A1, A2> T.asFunctionFrom(a1: Property<A1>,
                                                  a2: Property<A2>,
                                                  function: (A1, A2) -> R): T {
    forEachChange(a1, a2) {
        set(function(a1.get(), a2.get()))
    }
    return this
}

fun <T : Property<R>, R, A1, A2, A3> T.asFunctionFrom(a1: Property<A1>,
                                                      a2: Property<A2>,
                                                      a3: Property<A3>,
                                                      function: (A1, A2, A3) -> R): T {
    forEachChange(a1, a2, a3) {
        set(function(a1.get(), a2.get(), a3.get()))
    }
    return this
}

fun <T : Property<R>, R, A1, A2, A3, A4> T.asFunctionFrom(a1: Property<A1>,
                                                          a2: Property<A2>,
                                                          a3: Property<A3>,
                                                          a4: Property<A4>,
                                                          function: (A1, A2, A3, A4) -> R): T {
    forEachChange(a1, a2, a3, a4) {
        set(function(a1.get(), a2.get(), a3.get(), a4.get()))
    }
    return this
}

fun <T : Property<R>, R, A1, A2, A3, A4, A5> T.asFunctionFrom(a1: Property<A1>,
                                                              a2: Property<A2>,
                                                              a3: Property<A3>,
                                                              a4: Property<A4>,
                                                              a5: Property<A5>,
                                                              function: (A1, A2, A3, A4, A5) -> R): T {
    forEachChange(a1, a2, a3, a4, a5) {
        set(function(a1.get(), a2.get(), a3.get(), a4.get(), a5.get()))
    }
    return this
}

infix fun <A : Property<*>> TextView.showsTextFrom(property: A) {
    property.subscribe { text = it.toString() }
}

infix fun <A : Property<Int>> TextView.usesTextColorFrom(property: A) {
    property.subscribe { setTextColor(it) }
}

infix fun EditText.savesTextChangesTo(property: Property<String>) {
    property.set(text.toString())
    addTextChangedListener(object : BaseTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            property.set(s.toString())
        }
    })
}