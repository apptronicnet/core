package net.apptronic.core.entity.behavior

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.context.Context
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.EntitySubscription
import net.apptronic.core.entity.base.RelayEntity

fun <T> Entity<T>.withFirst(action: (T) -> Unit) {
    takeFirst().subscribe(action)
}

fun <T> Entity<T>.takeFirst(): Entity<T> {
    return take(1)
}

fun <T> Entity<T>.take(count: Int): Entity<T> {
    return TakeCountEntity(this, count)
}

private class TakeCountEntity<T>(
        source: Entity<T>,
        val count: Int
) : RelayEntity<T>(source) {

    override fun onNewObserver(targetContext: Context, observer: Observer<T>): Observer<T> {
        return CountObserver(count, observer)
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