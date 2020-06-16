package net.apptronic.core.component.entity.base

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subject.Subject
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.BaseEntity
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.subscriptions.subscriptionBuilder

/**
 * [Entity] which based on using [Observable] as source for subscriptions
 */
abstract class SubjectEntity<T> : BaseEntity<T>() {

    protected abstract val subject: Subject<T>

    final override fun onSubscribeObserver(targetContext: Context, targetObserver: Observer<T>): EntitySubscription {
        return subscriptionBuilder(targetContext).subscribe(targetObserver, subject)
    }

}

fun <T> subjectEntity(context: Context, subject: Subject<T>): SubjectEntity<T> {
    return SimpleSubjectEntity(context, subject)
}

private class SimpleSubjectEntity<T>(
        override val context: Context,
        override val subject: Subject<T>
) : SubjectEntity<T>()