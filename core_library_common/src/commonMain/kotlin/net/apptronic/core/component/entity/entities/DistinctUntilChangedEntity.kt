package net.apptronic.core.component.entity.entities

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription

fun <T> Entity<T>.distinctUntilChanged(): Entity<T> {
    return DistinctUntilChangedEntity(this)
}

class DistinctUntilChangedEntity<T>(
        private val source: Entity<T>
) : Entity<T> {

    override val context: Context = source.context

    override fun subscribe(context: Context, observer: Observer<T>): EntitySubscription {
        return source.subscribe(
                context,
                DistinctUntilChangedObserverWrapper(observer)
        )
    }

}

private class DistinctUntilChangedObserverWrapper<T>(
        val target: Observer<T>
) : Observer<T> {

    private var lastValue: ValueHolder<T>? = null

    override fun notify(value: T) {
        val last = lastValue
        if (last == null || last.value != value) {
            lastValue = ValueHolder(value)
            target.notify(value)
        }
    }

}