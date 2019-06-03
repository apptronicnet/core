package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.threading.WorkerDefinition
import net.apptronic.core.threading.execute

fun <T> Entity<T>.switchContext(
        context: Context,
        workerDefinition: WorkerDefinition = WorkerDefinition.DEFAULT
): Entity<T> {
    return if (getContext().getToken() != context.getToken()) {
        ContextSwitchEntity(
                this, context, workerDefinition
        )
    } else {
        this
    }
}

private class ContextSwitchEntity<T>(
        private val target: Entity<T>,
        private val targetContext: Context,
        private val targetWorkerDefinition: WorkerDefinition
) : Entity<T> {

    private val worker = targetContext.getScheduler().getWorker(targetWorkerDefinition)

    override fun getContext(): Context {
        return targetContext
    }

    override fun subscribe(observer: Observer<T>): EntitySubscription {
        return target.subscribe(targetContext) { value ->
            worker.execute {
                observer.notify(value)
            }
        }.also {
            getContext().getLifecycle().getRootStage().registerSubscription(it)
        }
    }

    override fun subscribe(context: Context, observer: Observer<T>): EntitySubscription {
        return target.subscribe(context, observer)
    }

}