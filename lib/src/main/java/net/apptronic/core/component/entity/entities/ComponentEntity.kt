package net.apptronic.core.component.entity.entities

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.PredicateObserver
import net.apptronic.core.component.entity.Subscription
import net.apptronic.core.component.entity.base.UpdatePredicate
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.threading.WorkerDefinition

abstract class ComponentEntity<T>(
    private val context: Context,
    protected val workingPredicate: UpdatePredicate<T>
) : Predicate<T> {

    private val stageWhenCreated = context.getLifecycle().getActiveStage()

    private var workerDefinition: WorkerDefinition? = null

    fun setWorker(workerDefinition: WorkerDefinition?) {
        this.workerDefinition = workerDefinition
    }

    override fun subscribe(observer: PredicateObserver<T>): Subscription {
        val subscription = workingPredicate.subscribe { value ->
            val workerToUse = workerDefinition ?: context.getScheduler().getDefaultWorker()
            context.getScheduler().execute(workerToUse) {
                observer.notify(value)
            }
        }
        context.getLifecycle().onExitFromActiveStage {
            subscription.unsubscribe()
        }
        return subscription
    }

}