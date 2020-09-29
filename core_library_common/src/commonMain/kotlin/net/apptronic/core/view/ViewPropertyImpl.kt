package net.apptronic.core.view

import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.base.EntityValue
import net.apptronic.core.component.entity.base.SubjectEntity
import net.apptronic.core.component.entity.base.UpdateEntity
import net.apptronic.core.component.entity.entities.distinctUntilChanged
import net.apptronic.core.component.entity.switchContext

interface ViewProperty<T> {

    fun getValue(): T

    fun subscribeWith(targetContext: Context, callback: (T) -> Unit)

}

internal class ViewPropertyImpl<T>(override val context: Context, initialValue: T) : SubjectEntity<T>(), EntityValue<T>, ViewProperty<T>, UpdateEntity<T> {

    override val subject: BehaviorSubject<T> = BehaviorSubject<T>()

    init {
        subject.update(initialValue)
    }

    override fun getValueHolder(): ValueHolder<T>? {
        return subject.getValue()
    }

    override fun update(value: T) {
        subject.update(value)
    }

    override fun getValue(): T {
        return super.get()
    }

    override fun subscribeWith(targetContext: Context, callback: (T) -> Unit) {
        if (targetContext == context) {
            distinctUntilChanged().subscribe(callback)
        } else {
            switchContext(targetContext).distinctUntilChanged().subscribe(callback)
        }
    }

}
