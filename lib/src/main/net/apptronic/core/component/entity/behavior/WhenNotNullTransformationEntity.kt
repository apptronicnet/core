package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.bindContext
import net.apptronic.core.component.entity.subscribe

fun <Source, Result> Entity<Source?>.whenNotNull(
    transformation: (Entity<Source>) -> Entity<Result>
): Entity<Result?> {
    return WhenNotNullTransformationEntity(this, transformation)
}

private class WhenNotNullTransformationEntity<Source, Result>(
    private val source: Entity<Source?>,
    transformation: (Entity<Source>) -> Entity<Result>
) : Entity<Result?> {

    private val sourcePredicate =
        BehaviorSubject<Source>()
    private val resultPredicate =
        BehaviorSubject<Result?>()

    init {
        transformation.invoke(sourcePredicate.bindContext(source.getContext())).subscribe {
            resultPredicate.update(it)
        }
        source.subscribe {
            if (it != null) {
                sourcePredicate.update(it)
            } else {
                resultPredicate.update(null)
            }
        }
    }

    override fun getContext(): Context {
        return source.getContext()
    }

    override fun subscribe(observer: Observer<Result?>): EntitySubscription {
        return resultPredicate.subscribe(observer).bindContext(source.getContext())
    }

}