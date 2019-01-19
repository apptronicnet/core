package net.apptronic.common.android.ui.viewmodel.entity

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import net.apptronic.common.android.ui.viewmodel.lifecycle.LifecycleHolder

abstract class ViewModelEvent<T>(lifecycleHolder: LifecycleHolder) : ViewModelSubjectEntity<T>(
    lifecycleHolder,
    EventEntitySubject(lifecycleHolder)
) {

    override fun asObservable(): Observable<T> {
        val result = PublishSubject.create<T>()
        subscribe {
            result.onNext(it)
        }
        return result
    }

}

class ViewModelTypedEvent<T>(lifecycleHolder: LifecycleHolder) :
    ViewModelEvent<T>(lifecycleHolder) {

    fun sendEvent(event: T) {
        onInput(event)
    }


}

class ViewModelGenericEvent(lifecycleHolder: LifecycleHolder) :
    ViewModelEvent<Unit>(lifecycleHolder) {

    fun sendEvent() {
        onInput(Unit)
    }

}