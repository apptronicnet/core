package net.apptronic.core.component.entity.subscriptions

import net.apptronic.core.base.concurrent.requireNeverFrozen
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subject.Subject
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.UpdateEntity

class ContextSubjectWrapper<T, E : Subject<T>>(
        override val context: Context,
        val wrapped: E
) : UpdateEntity<T> {

    init {
        requireNeverFrozen()
    }

    private val subscriptions = ContextSubscriptionFactory<T>(context)

    override fun update(value: T) {
        wrapped.update(value)
    }

    override fun subscribe(context: Context, observer: Observer<T>): EntitySubscription {
        return subscriptions.using(context).subscribe(observer, wrapped)
    }

}