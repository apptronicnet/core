package net.apptronic.core.android.viewmodel

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.entities.ComponentEvent
import net.apptronic.core.component.entity.entities.Property

fun EditText.bindTo(property: Property<String>) {
    property.subscribe {
        if (it != this.text.toString()) {
            this.setText(it)
            this.setSelection(it.length)
        }
    }
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            // ignore
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            // ignore
        }

        override fun afterTextChanged(s: Editable) {
            property.set(s.toString())
        }
    })
}

fun TextView.bindTo(property: Predicate<String>) {
    property.subscribe {
        this.text = it
    }
}

fun View.bindAsVisibleInvisible(property: Predicate<Boolean>) {
    property.subscribe {
        this.visibility = if (it) View.VISIBLE else View.INVISIBLE
    }
}

fun View.bindAsVisibleGone(property: Predicate<Boolean>) {
    property.subscribe {
        this.visibility = if (it) View.VISIBLE else View.GONE
    }
}

fun View.bindAsEnabledDisabled(property: Predicate<Boolean>) {
    property.subscribe {
        this.isEnabled = it
    }
}

fun View.bindOnClickListener(event: ComponentEvent<Unit>) {
    this.setOnClickListener {
        event.sendEvent(Unit)
    }
}

