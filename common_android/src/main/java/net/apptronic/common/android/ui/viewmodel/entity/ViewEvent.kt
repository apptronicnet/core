package net.apptronic.common.android.ui.viewmodel.entity

import android.text.Editable
import android.view.View
import android.widget.EditText
import net.apptronic.common.android.ui.utils.BaseTextWatcher
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleHolder

class ViewEvent<T>(lifecycleHolder: LifecycleHolder<*>) : SubjectEntity<T>(lifecycleHolder,
        EventSubject(lifecycleHolder.threadExecutor())) {

    fun sendEvent(event: T) {
        onInput(event)
    }

}

fun ViewEvent<Unit>.sendEvent() {
    sendEvent(Unit)
}

infix fun View.sendsClicksTo(viewEvent: ViewEvent<Unit>) {
    setOnClickListener { viewEvent.sendEvent() }
}


infix fun EditText.sendsTextChangeEventsTo(viewEvent: ViewEvent<String>) {
    addTextChangedListener(object : BaseTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            viewEvent.sendEvent(s.toString())
        }
    })
}