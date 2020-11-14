package net.apptronic.core.entity.base

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.subject.Subject
import net.apptronic.core.context.Context
import net.apptronic.core.entity.BaseEntity
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.EntitySubscription
import net.apptronic.core.entity.commons.performEntitySubscription

/**
 * [Entity] which based on using [Observable] as source for subscriptions
 */
abstract class ObservableSubjectEntity<T> : BaseEntity<T>() {

    protected abstract val subject: Subject<T>

    final override fun onSubscribeObserver(targetContext: Context, targetObserver: Observer<T>): EntitySubscription {
        return performEntitySubscription(targetContext, subject, targetObserver)
    }

}

fun <T> subjectEntity(context: Context, subject: Subject<T>): ObservableSubjectEntity<T> {
    return SimpleSubjectEntity(context, subject)
}

private class SimpleSubjectEntity<T>(
        override val context: Context,
        override val subject: Subject<T>
) : ObservableSubjectEntity<T>()