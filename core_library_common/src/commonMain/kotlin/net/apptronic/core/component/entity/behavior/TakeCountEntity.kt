package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription

fun <T> Entity<T>.takeFirst(): Entity<T> {
    return TakeCountEntity(this, 1)
}

fun <T> Entity<T>.take(count: Int): Entity<T> {
    return TakeCountEntity(this, count)
}

fun <T> Entity<T>.takeOne(): Entity<T> {
    return take(1)
}

private class TakeCountEntity<T>(
        val source: Entity<T>,
        val count: Int
) : Entity<T> {

    override val context: Context = source.context

    override fun subscribe(context: Context, observer: Observer<T>): EntitySubscription {
        val targetObserver = CountObserver(count, observer)
        val subscription = source.subscribe(context, targetObserver)
        targetObserver.subscription = subscription
        return subscription
    }

    private class CountObserver<T>(
            val max: Int,
            val target: Observer<T>
    ) : Observer<T> {
        private var count = 0
        var subscription: EntitySubscription? = null
            set(value) {
                if (count >= max) {
                    value?.unsubscribe()
                } else {
                    field = value
                }
            }

        override fun notify(value: T) {
            if (count < max) {
                count++
                target.notify(value)
            }
            if (count >= max) {
                subscription?.unsubscribe()
            }
        }
    }

}