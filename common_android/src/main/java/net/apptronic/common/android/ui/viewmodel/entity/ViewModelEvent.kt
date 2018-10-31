package net.apptronic.common.android.ui.viewmodel.entity

import android.text.Editable
import android.view.View
import android.widget.EditText
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import net.apptronic.common.android.ui.utils.BaseTextWatcher
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleHolder

abstract class ViewModelEvent<T>(lifecycleHolder: LifecycleHolder<*>) : ViewModelSubjectEntity<T>(lifecycleHolder,
        EventEntitySubject(lifecycleHolder.threadExecutor())) {

    override fun asObservable(): Observable<T> {
        val result = PublishSubject.create<T>()
        subscribe {
            result.onNext(it)
        }
        return result
    }

}

class ViewModelTypedEvent<T>(lifecycleHolder: LifecycleHolder<*>) : ViewModelEvent<T>(lifecycleHolder) {

    fun sendEvent(event: T) {
        onInput(event)
    }


}

class ViewModelGenericEvent(lifecycleHolder: LifecycleHolder<*>) : ViewModelEvent<Unit>(lifecycleHolder) {

    fun sendEvent() {
        onInput(Unit)
    }

}

infix fun View.sendsClicksTo(viewEvent: ViewModelGenericEvent) {
    setOnClickListener { viewEvent.sendEvent() }
}


infix fun EditText.sendsTextChangeEventsTo(viewEvent: ViewModelTypedEvent<String>) {
    addTextChangedListener(object : BaseTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            viewEvent.sendEvent(s.toString())
        }
    })
}