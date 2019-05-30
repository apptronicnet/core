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

    private val sourceSubject = BehaviorSubject<Source>()
    private val resultSubject = ContextSubjectWrapper(getContext(), BehaviorSubject<Result?>())

    init {
        transformation.invoke(sourceSubject.bindContext(source.getContext())).subscribe {
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

    override fun getContext(): Context {
        return source.getContext()
    }

    override fun subscribe(context: Context, observer: Observer<Result?>): EntitySubscription {
        return resultSubject.subscribe(context, observer)
    }

}