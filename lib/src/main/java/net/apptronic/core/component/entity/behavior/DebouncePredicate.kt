package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.AtomicEntity
import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.PredicateObserver
import net.apptronic.core.component.entity.Subscription
import net.apptronic.core.component.entity.base.UpdateAndStorePredicate
import net.apptronic.core.component.entity.base.ValueHolder
import net.apptronic.core.component.entity.subscribe

fun <Source, Result> Predicate<Source>.debounce(
    debouncedTransformation: (Predicate<Source>) -> Predicate<Result>
): Predicate<Result> {
    return DebouncePredicate(this, debouncedTransformation)
}

private class DebouncePredicate<Source, Result>(
    sourcePredicate: Predicate<Source>,
    debouncedTransformation: (Predicate<Source>) -> Predicate<Result>
) : Predicate<Result> {

    private val requestPredicate = UpdateAndStorePredicate<Source>()
    private val resultPredicate = UpdateAndStorePredicate<Result>()

    private val awaitingValue = AtomicEntity<ValueHolder<Source>?>(null)
    private val isProcessing = AtomicEntity(false)

    init {
        sourcePredicate.subscribe { nextSource ->
            awaitingValue.perform {
                set(ValueHolder(nextSource))
            }
            takeNext()
        }
        debouncedTransformation.invoke(requestPredicate).subscribe { nextResult ->
            resultPredicate.update(nextResult)
            awaitingValue.perform {
                isProcessing.set(false)
            }
            takeNext()
        }
    }

    private fun takeNext() {
        awaitingValue.perform {
            val valueHolder = get()
            if (valueHolder != null && isProcessing.get().not()) {
                requestPredicate.update(valueHolder.value)
                isProcessing.set(true)
                this.set(null)
            }
        }
    }

    override fun subscribe(observer: PredicateObserver<Result>): Subscription {
        return resultPredicate.subscribe(observer)
    }

}