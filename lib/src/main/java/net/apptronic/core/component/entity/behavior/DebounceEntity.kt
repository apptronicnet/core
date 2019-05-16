package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.AtomicEntity
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.bindContext

fun <Source, Result> Entity<Source>.debounce(
    debouncedTransformation: (Entity<Source>) -> Entity<Result>
): Entity<Result> {
    return DebounceEntity(this, debouncedTransformation)
}

private class DebounceEntity<Source, Result>(
    private val sourceEntity: Entity<Source>,
    debouncedTransformation: (Entity<Source>) -> Entity<Result>
) : Entity<Result> {

    private val sourceObservable =
        BehaviorSubject<Source>()
    private val resultObservable =
        BehaviorSubject<Result>()

    private val awaitingValue = AtomicEntity<ValueHolder<Source>?>(null)
    private val isProcessing = AtomicEntity(false)

    init {
        sourceEntity.subscribe { nextSource ->
            awaitingValue.perform {
                set(ValueHolder(nextSource))
            }
            takeNext()
        }
        debouncedTransformation.invoke(sourceObservable.bindContext(sourceEntity.getContext()))
            .subscribe { nextResult ->
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
        return resultObservable.subscribe(observer).bindContext(sourceEntity.getContext())
    }

}