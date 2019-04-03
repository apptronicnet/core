package net.apptronic.core.component.entity.entities

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.Subscription
import net.apptronic.core.component.entity.base.UpdatePredicate

abstract class ComponentEntity<T>(
    private val context: Context,
    protected val workingPredicate: UpdatePredicate<T>
) : Predicate<T> {

    private val stageWhenCreated = context.getLifecycle().getActiveStage()

    private var workerName: String? = null

    fun setWorker(workerName: String?) {
        this.workerName = workerName
    }

    override fun subscribe(observer: (T) -> Unit): Subscription {
        val subscription = workingPredicate.subscribe { value ->
            val workerToUse = workerName ?: context.getWorkers().getDefaultWorker()
            context.getWorkers().execute(workerToUse) {
                observer.invoke(value)
            }
        }
        context.getLifecycle().onExitFromActiveStage {
            subscription.unsubscribe()
        }
        return subscription
    }

}