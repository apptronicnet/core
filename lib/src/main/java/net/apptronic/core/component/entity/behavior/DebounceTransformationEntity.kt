package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.AtomicEntity
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.bindContext
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.threading.WorkerDefinition

fun <Source, Result> Entity<Source>.debounce(
    transformWith: WorkerDefinition = WorkerDefinition.BACKGROUND_PARALLEL_SHARED,
    subscribeWith: WorkerDefinition = WorkerDefinition.DEFAULT,
    debouncedTransformation: (Entity<Source>) -> Entity<Result>
): Entity<Result> {
    return DebounceTransformationEntity(
        this,
        transformWith,
        subscribeWith,
        debouncedTransformation
    )
}

private class DebounceTransformationEntity<Source, Result>(
    private val sourceEntity: Entity<Source>,
    private val transformWorkerDefinition: WorkerDefinition,
    private val subscribeWorkerDefinition: WorkerDefinition,
    debouncedTransformation: (Entity<Source>) -> Entity<Result>
) : Entity<Result> {

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
        val sourceEntity = sourceObservable.bindContext(sourceEntity.getContext())
            .switchWorker(transformWorkerDefinition)
        debouncedTransformation.invoke(sourceEntity).subscribe { nextResult ->
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

    override fun getContext(): Context {
        return sourceEntity.getContext()
    }

    override fun subscribe(observer: Observer<Result>): EntitySubscription {
        return resultObservable.bindContext(sourceEntity.getContext())
            .switchWorker(subscribeWorkerDefinition)
            .subscribe(observer)
    }

}