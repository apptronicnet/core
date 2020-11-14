package net.apptronic.core.entity.behavior

import net.apptronic.core.base.subject.BehaviorSubject
import net.apptronic.core.base.subject.Subject
import net.apptronic.core.context.Context
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.ObservableSubjectEntity
import net.apptronic.core.entity.base.subjectEntity

fun <Source, Result> Entity<Source?>.whenNotNull(
        transformation: (Entity<Source>) -> Entity<Result>
): Entity<Result?> {
    return WhenNotNullTransformationEntity(this, transformation)
}

private class WhenNotNullTransformationEntity<Source, Result>(
        private val source: Entity<Source?>,
        transformation: (Entity<Source>) -> Entity<Result>
) : ObservableSubjectEntity<Result?>() {

    override val context: Context = source.context
    private val sourceSubject = BehaviorSubject<Source>()
    override val subject: Subject<Result?> = BehaviorSubject<Result?>()

    init {
        val nonNullSource = subjectEntity(context, sourceSubject)
        val nonNullResult = transformation.invoke(nonNullSource)
        nonNullResult.subscribe(context) {
            subject.update(it)
        }
        source.subscribe(context) {
            if (it != null) {
                sourceSubject.update(it)
            } else {
                subject.update(null)
            }
        }
    }

}