package net.apptronic.common.android.ui.viewmodel.entity

import android.text.Editable
import android.view.View
import android.widget.EditText
import net.apptronic.common.android.ui.utils.BaseTextWatcher
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleHolder

class UserAction<T>(lifecycleHolder: LifecycleHolder<*>) : SubjectEntity<T>(lifecycleHolder,
        EventSubject(lifecycleHolder.threadExecutor())) {

    fun sendEvent(event: T) {
        onInput(event)
    }

}

fun UserAction<Unit>.sendEvent() {
    sendEvent(Unit)
}

infix fun View.sendsClicksTo(userAction: UserAction<Unit>) {
    setOnClickListener { userAction.sendEvent() }
}


infix fun EditText.sendsTextChangeEventsTo(userAction: UserAction<String>) {
    addTextChangedListener(object : BaseTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            userAction.sendEvent(s.toString())
        }
    })
}