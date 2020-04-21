package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
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
) : Entity<Result?> {

    override val context: Context = source.context

    private val sourceSubject = BehaviorSubject<Source>()
    private val resultSubject = ContextSubjectWrapper(context, BehaviorSubject<Result?>())

    init {
        transformation.invoke(sourceSubject.bindContext(context)).subscribe {
            resultSubject.update(it)
        }
        source.subscribe {
            if (it != null) {
                sourceSubject.update(it)
            } else {
                resultSubject.update(null)
            }
        }
    }

    override fun subscribe(context: Context, observer: Observer<Result?>): EntitySubscription {
        return resultSubject.subscribe(context, observer)
    }

}