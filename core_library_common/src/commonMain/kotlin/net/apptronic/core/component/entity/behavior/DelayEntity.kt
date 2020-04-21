package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.subscribe

fun <T> Entity<T>.delay(
        delay: Long = 0
): Entity<T> {
    return DelayEntity(this, delay)
}

private class DelayEntity<T>(
        val target: Entity<T>,
        val delay: Long
) : Entity<T> {

    override val context: Context = target.context

    override fun subscribe(context: Context, observer: Observer<T>): EntitySubscription {
        val activeStage = context.getLifecycle().getActiveStage()
        return target.subscribe(context) { next ->
            activeStage?.launchCoroutine {
                kotlinx.coroutines.delay(delay)
                observer.notify(next)
            }
        }
    }

}