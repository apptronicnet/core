package net.apptronic.core.component.entity.behavior

import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.PredicateObserver
import net.apptronic.core.component.entity.Subscription
import net.apptronic.core.component.entity.base.UpdateAndStorePredicate
import net.apptronic.core.component.entity.subscribe

fun <Source, Result> Predicate<Source?>.whenNotNull(
    transformation: (Predicate<Source>) -> Predicate<Result>
): Predicate<Result?> {
    return WhenNotNullTransformationPredicate(this, transformation)
}

private class WhenNotNullTransformationPredicate<Source, Result>(
    source: Predicate<Source?>,
    transformation: (Predicate<Source>) -> Predicate<Result>
) : Predicate<Result?> {

    private val sourcePredicate = UpdateAndStorePredicate<Source>()
    private val resultPredicate = UpdateAndStorePredicate<Result?>()

    init {
        transformation.invoke(sourcePredicate).subscribe {
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

    override fun subscribe(observer: PredicateObserver<Result?>): Subscription {
        return resultPredicate.subscribe(observer)
    }

}