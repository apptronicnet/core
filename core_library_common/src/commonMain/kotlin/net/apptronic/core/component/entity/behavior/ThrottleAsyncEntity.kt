package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.component.entity.subscriptions.ContextSubjectWrapper

/**
 * Consumes source entity asynchronously taking only last emitted value on each change synchronous. If source value
 * changing synchronously many times it will emit only last value.
 */
fun <T> Entity<T>.throttleAsync(): Entity<T> {
    return ThrottleAsyncEntity(this)
}

private class ThrottleAsyncEntity<T>(
        private val source: Entity<T>
) : Entity<T> {

    override val context: Context = source.context

    private var nextValue: ValueHolder<T>? = null
    private var isProcessing = false
    private val subject = ContextSubjectWrapper(context, BehaviorSubject<T>())

    init {
        val activeStage = context.getLifecycle().getActiveStage()
        source.subscribe {
            nextValue = ValueHolder(it)
            if (!isProcessing) {
                isProcessing = true
                activeStage?.launchCoroutine {
                    isProcessing = false
                    nextValue?.let {
                        subject.update(it.value)
                    }
                    nextValue = null
                }
            }
        }
    }

    override fun subscribe(context: Context, observer: Observer<T>): EntitySubscription {
        return subject.subscribe(context, observer)
    }

}