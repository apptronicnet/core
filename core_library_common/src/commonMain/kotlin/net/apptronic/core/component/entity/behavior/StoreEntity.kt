package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.EntityValue
import net.apptronic.core.component.entity.subscriptions.ContextSubjectWrapper

fun <T> Entity<T>.storeLatest(): EntityValue<T> {
    return StoreEntity(this)
}

private class StoreEntity<T>(
        val target: Entity<T>
) : EntityValue<T> {

    private val behaviorSubject = BehaviorSubject<T>()
    private val subject = ContextSubjectWrapper(target.getContext(), behaviorSubject)

    override fun getContext(): Context {
        return target.getContext()
    }

    init {
        target.subscribe(subject)
    }

    override fun getValueHolder(): ValueHolder<T>? {
        return behaviorSubject.getValue()
    }

    override fun subscribe(context: Context, observer: Observer<T>): EntitySubscription {
        return subject.subscribe(context, observer)
    }

}