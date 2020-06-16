package net.apptronic.core.component.entity.base

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subject.Subject
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.BaseEntity
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.entities.performEntitySubscription

/**
 * [Entity] which based on using [Observable] as source for subscriptions
 */
abstract class ObservableEntity<T> : BaseEntity<T>() {

    abstract override val context: Context

    protected abstract val observable: Observable<T>

    final override fun onSubscribeObserver(targetContext: Context, targetObserver: Observer<T>): EntitySubscription {
        return performEntitySubscription(targetContext, observable, targetObserver)
    }

}

fun <T> observableEntity(context: Context, subject: Subject<T>): ObservableEntity<T> {
    return SimpleObservableEntity(context, subject)
}

private class SimpleObservableEntity<T>(
        override val context: Context,
        override val observable: Observable<T>
) : ObservableEntity<T>()