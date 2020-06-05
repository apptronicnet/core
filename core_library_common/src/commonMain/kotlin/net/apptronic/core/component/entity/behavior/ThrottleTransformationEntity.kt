package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.BaseEntity
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.entities.Value
import net.apptronic.core.component.entity.functions.onNext
import net.apptronic.core.component.entity.subscribe

/**
 * Throttles source observable to prevent parallel processing many items at same time. Emits next item for processing
 * only when previous item was processed. Throws out from chain old items if new arrived before previous started
 * processing.
 * @param throttledTransformation method to operate throttled input, should returns result entity to be processed
 * after throttling of throttling chain will be broken and never emit next value.
 */
fun <Source, Result> Entity<Source>.throttle(
        throttledTransformation: (Entity<Source>) -> Entity<Result>
): Entity<Result> {
    return ThrottleTransformationEntity(
            this,
            throttledTransformation
    )
}

private class ThrottleTransformationEntity<Source, Result>(
        private val sourceEntity: Entity<Source>,
        private val throttledTransformation: (Entity<Source>) -> Entity<Result>
) : BaseEntity<Result>(), Observer<Result> {

    override val context: Context = sourceEntity.context

    private val resultObservable = Value<Result>(context)

    init {
        val throttledTransformationResult = ThrottledTransformation(context, throttledTransformation)
        throttledTransformationResult.observe(this@ThrottleTransformationEntity)
        sourceEntity.onNext { nextValue ->
            throttledTransformationResult.onNext(nextValue)
        }
    }

    override fun subscribe(targetContext: Context, observer: Observer<Result>): EntitySubscription {
        return resultObservable.subscribe(targetContext, observer)
    }

    override fun notify(value: Result) {
        resultObservable.notify(value)
    }

}

private class ThrottledTransformation<Source, Result>(
        context: Context,
        throttledTransformation: (Entity<Source>) -> Entity<Result>
) {

    private val sourceEntity = Value<Source>(context)
    private val resultObservable = BehaviorSubject<Result>()

    private var awaitingValue: ValueHolder<Source>? = null
    private var isProcessing = false

    fun onNext(value: Source) {
        awaitingValue = ValueHolder(value)
        takeNext()
    }

    fun observe(observer: Observer<Result>) {
        resultObservable.subscribe(observer)
    }

    init {
        throttledTransformation(sourceEntity).subscribe { nextResult ->
            resultObservable.update(nextResult)
            isProcessing = false
            takeNext()
        }
    }

    private fun takeNext() {
        awaitingValue?.let {
            if (!isProcessing) {
                sourceEntity.update(it.value)
                isProcessing = true
                awaitingValue = null
            }
        }
    }

}