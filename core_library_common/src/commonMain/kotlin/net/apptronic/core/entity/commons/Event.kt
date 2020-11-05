package net.apptronic.core.entity.commons

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.subject.PublishSubject
import net.apptronic.core.context.Context
import net.apptronic.core.entity.base.SubjectEntity
import net.apptronic.core.entity.base.UpdateEntity

abstract class Event<T>(override val context: Context) : SubjectEntity<T>(), UpdateEntity<T> {

    override val subject = PublishSubject<T>()

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

fun GenericEvent.subscribe(action: () -> Unit) {
    this.subscribe(object : Observer<Unit> {
        override fun notify(value: Unit) {
            action.invoke()
        }
    })
}