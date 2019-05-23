package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.subscribe

fun <T> Entity<T>.suspendOn(timeInMillis: Long): Entity<T> {
    return SuspendEntity(this) { timeInMillis }
}

fun <T> Entity<T>.suspendOn(timeInMillisProvider: (T) -> Long): Entity<T> {
    return SuspendEntity(this, timeInMillisProvider)
}

private class SuspendEntity<T>(
    private val source: Entity<T>,
    private val timeInMillisProvider: (T) -> Long
) : Entity<T> {

    private val platformHandler = source.getContext().getPlatformHandler()

    override fun getContext(): Context {
        return source.getContext()
    }

    override fun subscribe(observer: Observer<T>): EntitySubscription {
        return source.subscribe {
            val timeInMillis = timeInMillisProvider.invoke(it)
            if (timeInMillis > 0L) {
                platformHandler.suspendCurrentThread(timeInMillis)
            }
            observer.notify(it)
        }
    }

}