package net.apptronic.core.entity.commons

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.subject.PublishSubject
import net.apptronic.core.context.Context
import net.apptronic.core.entity.base.ObservableSubjectEntity
import net.apptronic.core.entity.base.SubjectEntity

abstract class Event<T>(override val context: Context) : ObservableSubjectEntity<T>(), SubjectEntity<T> {

    override val subject = PublishSubject<T>()

    override fun update(value: T) {
        subject.update(value)
    }

}

class GenericEvent(context: Context) : Event<Unit>(context) {

    fun update() {
        update(Unit)
    }

}

class TypedEvent<T>(context: Context) : Event<T>(context)

fun GenericEvent.subscribe(action: () -> Unit) {
    this.subscribe(object : Observer<Unit> {
        override fun update(value: Unit) {
            action.invoke()
        }
    })
}