package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.component.entity.subscriptions.ContextSubjectWrapper
import net.apptronic.core.threading.WorkerDefinition
import net.apptronic.core.threading.execute

/**
 * Consumes source entity asynchronously taking only last emitted value on each change synchronous. If source value
 * changing synchronously many times it will emit only last value.
 */
fun <T> Entity<T>.throttleAsync(): Entity<T> {
    return ThrottleAsyncEntity(switchWorker(WorkerDefinition.DEFAULT))
}

private class ThrottleAsyncEntity<T>(
        private val source: Entity<T>
) : Entity<T> {

    private val asyncWorker = source.getContext().getScheduler().getWorker(WorkerDefinition.DEFAULT_ASYNC)
    private var nextValue: ValueHolder<T>? = null
    private var isProcessing = false
    private val subject = ContextSubjectWrapper(source.getContext(), BehaviorSubject<T>())

    init {
        source.subscribe {
            nextValue = ValueHolder(it)
            if (!isProcessing) {
                isProcessing = true
                asyncWorker.execute {
                    isProcessing = false
                    nextValue?.let {
                        subject.update(it.value)
                    }
                    nextValue = null
                }
            }
        }
    }

    override fun getContext(): Context {
        return subject.getContext()
    }

    override fun subscribe(context: Context, observer: Observer<T>): EntitySubscription {
        return subject.subscribe(context, observer)
    }

}