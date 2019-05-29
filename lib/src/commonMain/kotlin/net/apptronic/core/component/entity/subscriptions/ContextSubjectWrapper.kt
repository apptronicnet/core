package net.apptronic.core.component.entity.subscriptions

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subject.Subject
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.EntitySubscription

class ContextSubjectWrapper<T, E : Subject<T>>(
        context: Context,
        val wrapped: E
) : Subject<T> {

    private val subscriptions = ContextSubscriptions<T>(context)

    override fun update(value: T) {
        wrapped.update(value)
    }

    override fun subscribe(observer: Observer<T>): EntitySubscription {
        return subscriptions.subscribe(observer, wrapped)
    }

}