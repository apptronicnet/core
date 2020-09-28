package net.apptronic.core.view

import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.base.EntityValue
import net.apptronic.core.component.entity.base.SubjectEntity
import net.apptronic.core.component.entity.base.UpdateEntity

interface ViewProperty<T> : Entity<T>, EntityValue<T>

internal class ViewPropertyImpl<T>(override val context: Context, initialValue: T) : SubjectEntity<T>(), ViewProperty<T>, UpdateEntity<T> {

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

}
