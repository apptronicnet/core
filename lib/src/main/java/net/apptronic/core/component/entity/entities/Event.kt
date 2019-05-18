package net.apptronic.core.component.entity.entities

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.subject.PublishSubject
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.UpdateEntity

abstract class Event<T>(context: Context) : ComponentEntity<T>(context), UpdateEntity<T> {

    private val subject = PublishSubject<T>()

    override fun getObservable(): Observable<T> {
        return subject
    }

    fun sendEvent(event: T) {
        subject.update(event)
    }

    override fun update(value: T) {
        sendEvent(value)
    }

}

class GenericEvent(context: Context) : Event<Unit>(context) {

    fun sendEvent() {
        sendEvent(Unit)
    }

}

class TypedEvent<T>(context: Context) : Event<T>(context) {

    fun send(event: T) {
        sendEvent(event)
    }

}