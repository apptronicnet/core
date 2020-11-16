package net.apptronic.core.entity.behavior

import kotlinx.coroutines.CoroutineScope
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.subject.BehaviorSubject
import net.apptronic.core.base.subject.ValueHolder
import net.apptronic.core.context.Context
import net.apptronic.core.entity.BaseEntity
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.EntitySubscription
import net.apptronic.core.entity.commons.BaseMutableValue
import net.apptronic.core.entity.function.mapSuspend

/**
 * Throttles source observable to prevent parallel processing many items at same time. Emits next item for processing
 * only when previous item was processed. Throws out from chain old items if new arrived before previous started
 * processing.
 * @param mapping mapping to be performed
 */
fun <Source, Result> Entity<Source>.throttleMap(
        mapping: suspend CoroutineScope.(Source) -> Result
): Entity<Result> {
    return ThrottledMapEntity(this, mapping)
}

fun <Source, Result> Entity<Source?>.throttleMapOrNull(
        mapping: suspend CoroutineScope.(Source) -> Result
): Entity<Result?> {
    return throttleMap {
        if (it == null) {
            null
        } else {
            mapping(it)
        }
    }
}

private class ThrottledMapEntity<Source, Result>(
        private val sourceEntity: Entity<Source>,
        private val mapping: suspend CoroutineScope.(Source) -> Result
) : BaseEntity<Result>(), Observer<Result> {

    override val context: Context = sourceEntity.context

    private val resultObservable = BaseMutableValue<Result>(context)

    init {
        val throttledTransformationResult = ThrottledTransformation(context, mapping)
        throttledTransformationResult.observe(this@ThrottledMapEntity)
        sourceEntity.subscribe {
            throttledTransformationResult.onNext(it)
        }
    }

    override fun onSubscribeObserver(targetContext: Context, targetObserver: Observer<Result>): EntitySubscription {
        return resultObservable.subscribe(targetContext, targetObserver)
    }

    override fun update(value: Result) {
        resultObservable.update(value)
    }

}

private class ThrottledTransformation<Source, Result>(
        context: Context,
        mapping: suspend CoroutineScope.(Source) -> Result
) {

    private val sourceEntity = BaseMutableValue<Source>(context)
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
        sourceEntity.mapSuspend(mapping).subscribe(context) { nextResult ->
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