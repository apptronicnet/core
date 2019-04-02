package net.apptronic.core.component.entity.entities

import net.apptronic.core.component.context.ComponentContext
import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.Subscription
import net.apptronic.core.component.entity.base.UpdatePredicate

abstract class ComponentEntity<T>(
    private val context: ComponentContext,
    protected val workingPredicate: UpdatePredicate<T>
) : Predicate<T> {

    private val stageWhenCreated = context.getLifecycle().getActiveStage()

    private var workerName: String? = null

    fun setWorker(workerName: String?) {
        this.workerName = workerName
    }

    override fun subscribe(observer: (T) -> Unit): Subscription {
        val subscription = workingPredicate.subscribe { value ->
            val workerToUse = workerName ?: context.workers().getDefaultWorker()
            context.workers().execute(workerToUse) {
                observer.invoke(value)
            }
        }
        context.getLifecycle().onExitFromActiveStage {
            subscription.unsubscribe()
        }
        return subscription
    }

}