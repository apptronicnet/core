package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subject.PublishSubject
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.subscriptions.ContextSubjectWrapper

fun <T> Entity<T>.asEvent(): Entity<T> {
    return EventEntity(this)
}

private class EventEntity<T>(
        val target: Entity<T>
) : Entity<T> {

    private val subject = ContextSubjectWrapper(target.getContext(), PublishSubject<T>())

    override fun getContext(): Context {
        return target.getContext()
    }

    init {
        target.subscribe(subject)
    }

    override fun subscribe(context: Context, observer: Observer<T>): EntitySubscription {
        return subject.subscribe(context, observer)
    }

}