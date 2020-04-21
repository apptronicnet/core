package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.concurrent.AtomicEntity
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.bindContext
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.threading.WorkerDefinition

/**
 * Throttles source observable to prevent parallel processing many items at same time. Emits next item for processing
 * only when previous item was processed. Throws out from chain old items if new arrived before previous started
 * processing.
 * @param throttledTransformation method tpo operate throttled input, should returns result entity to be processet
 * after throttling
 */
fun <Source, Result> Entity<Source>.throttle(
        transformWith: WorkerDefinition = WorkerDefinition.BACKGROUND_PARALLEL_SHARED,
        subscribeWith: WorkerDefinition = WorkerDefinition.DEFAULT,
        throttledTransformation: (Entity<Source>) -> Entity<Result>
): Entity<Result> {
    return ThrottleTransformationEntity(
        this,
        transformWith,
        subscribeWith,
            throttledTransformation
    )
}

private class ThrottleTransformationEntity<Source, Result>(
        private val sourceEntity: Entity<Source>,
        private val transformWorkerDefinition: WorkerDefinition,
        private val subscribeWorkerDefinition: WorkerDefinition,
        private val throttledTransformation: (Entity<Source>) -> Entity<Result>
) : Entity<Result> {

    override val context: Context = sourceEntity.context

    private val sourceObservable = BehaviorSubject<Source>()
    private val resultObservable = BehaviorSubject<Result>()

    private val awaitingValue = AtomicEntity<ValueHolder<Source>?>(null)
    private val isProcessing = AtomicEntity(false)

    init {
        sourceEntity.subscribe { nextSource ->
            awaitingValue.perform {
                set(ValueHolder(nextSource))
            }
            takeNext()
        }
        val sourceEntity = sourceObservable.bindContext(context)
                .switchWorker(transformWorkerDefinition)
        throttledTransformation.invoke(sourceEntity).subscribe { nextResult ->
            resultObservable.update(nextResult)
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
                sourceObservable.update(valueHolder.value)
                isProcessing.set(true)
                this.set(null)
            }
        }
    }

    override fun subscribe(observer: Observer<Result>): EntitySubscription {
        return resultObservable.bindContext(context)
                .switchWorker(subscribeWorkerDefinition)
                .subscribe(observer)
    }

    override fun subscribe(context: Context, observer: Observer<Result>): EntitySubscription {
        return resultObservable.bindContext(context)
                .switchWorker(subscribeWorkerDefinition)
                .subscribe(observer)
    }

}