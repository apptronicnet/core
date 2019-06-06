package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.platform.pauseCurrentThread
import net.apptronic.core.threading.WorkerDefinition
import net.apptronic.core.threading.execute

fun <T> Entity<T>.delay(
        delay: Long = 0,
        targetWorker: WorkerDefinition = WorkerDefinition.DEFAULT
): Entity<T> {
    return DelayEntity(this, delay, targetWorker)
}

private class DelayEntity<T>(
        val target: Entity<T>,
        val delay: Long,
        val targetWorker: WorkerDefinition
) : Entity<T> {

    init {
        target.subscribe {
            target.getContext().getScheduler().execute(WorkerDefinition.TIMER) {
                pauseCurrentThread(delay)
            }
        }
    }

    override fun getContext(): Context {
        return target.getContext()
    }

    override fun subscribe(context: Context, observer: Observer<T>): EntitySubscription {
        val worker = context.getScheduler().getWorker(targetWorker)
        return target.subscribe(context) { next ->
            target.getContext().getScheduler().execute(WorkerDefinition.TIMER) {
                pauseCurrentThread(delay)
                worker.execute {
                    observer.notify(next)
                }
            }
        }
    }

}