package net.apptronic.common.android.ui.viewmodel.entity

import android.text.Editable
import android.view.View
import android.widget.EditText
import net.apptronic.common.android.ui.utils.BaseTextWatcher
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleHolder

class UserAction<T>(lifecycleHolder: LifecycleHolder<*>) : SubjectEntity<T>(lifecycleHolder,
        EventSubject(lifecycleHolder.threadExecutor())) {

    fun sendEvent() {
        onInput(null)
    }

    fun sendEvent(event: T) {
        onInput(event)
    }

}

infix fun View.sendClicksTo(userAction: UserAction<*>) {
    setOnClickListener { userAction.sendEvent() }
}


infix fun EditText.sendTextChangeEventsTo(userAction: UserAction<String>) {
    addTextChangedListener(object : BaseTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            userAction.sendEvent(s.toString())
        }
    })
}