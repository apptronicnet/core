package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.base.observable.subject.Subject
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.base.SubjectEntity
import net.apptronic.core.component.entity.base.subjectEntity
import net.apptronic.core.component.entity.bindContext
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.component.entity.subscriptions.ContextSubjectWrapper

fun <Source, Result> Entity<Source?>.whenNotNull(
        transformation: (Entity<Source>) -> Entity<Result>
): Entity<Result?> {
    return WhenNotNullTransformationEntity(this, transformation)
}

private class WhenNotNullTransformationEntity<Source, Result>(
        private val source: Entity<Source?>,
        transformation: (Entity<Source>) -> Entity<Result>
) : SubjectEntity<Result?>() {

    override val context: Context = source.context
    private val sourceSubject = BehaviorSubject<Source>()
    override val subject: Subject<Result?> = BehaviorSubject<Result?>()

    init {
        val nonNullSource = subjectEntity(context, sourceSubject)
        val nonNullResult = transformation.invoke(nonNullSource)
        nonNullResult.subscribe {
            subject.update(it)
        }
        source.subscribe {
            if (it != null) {
                sourceSubject.update(it)
            } else {
                subject.update(null)
            }
        }
    }

}