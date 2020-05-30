package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.base.EntityValue
import net.apptronic.core.component.entity.base.SubjectEntity
import net.apptronic.core.component.entity.subscriptions.ContextSubjectWrapper

fun <T> Entity<T>.storeLatest(): EntityValue<T> {
    return StoreEntity(this)
}

private class StoreEntity<T>(
        val target: Entity<T>
) : SubjectEntity<T>(), EntityValue<T> {

    override val context: Context = target.context

    override val subject = BehaviorSubject<T>()

    init {
        target.subscribe(subject)
    }

    override fun getValueHolder(): ValueHolder<T>? {
        return subject.getValue()
    }

}