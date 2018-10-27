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

fun UserAction<*>.handleClicks(view: View) {
    view.setOnClickListener { sendEvent() }
}

fun UserAction<String>.handleInput(view: EditText) {
    view.addTextChangedListener(object : BaseTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            sendEvent(s.toString())
        }
    })
}