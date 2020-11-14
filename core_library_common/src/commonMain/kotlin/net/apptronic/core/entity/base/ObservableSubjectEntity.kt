package net.apptronic.core.entity.base

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.subject.Subject
import net.apptronic.core.context.Context
import net.apptronic.core.entity.Entity

/**
 * [Entity] which based on using [Observable] as source for subscriptions
 */
abstract class ObservableSubjectEntity<T> : ObservableEntity<T>() {

    protected abstract val subject: Subject<T>

    override val observable: Observable<T>
        get() = subject

}

fun <T> subjectEntity(context: Context, subject: Subject<T>): ObservableSubjectEntity<T> {
    return SimpleSubjectEntity(context, subject)
}

private class SimpleSubjectEntity<T>(
        override val context: Context,
        override val subject: Subject<T>
) : ObservableSubjectEntity<T>()