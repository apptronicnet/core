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

private class TakeCountEntity<T>(
    val source: Entity<T>,
    val count: Int
) : Entity<T> {

    override fun getContext(): Context {
        return source.getContext()
    }

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
                if (max >= count) {
                    value?.unsubscribe()
                } else {
                    field = value
                }
            }

        override fun notify(value: T) {
            if (max < count) {
                count++
                target.notify(value)
            } else {
                subscription?.unsubscribe()
            }
        }
    }

}