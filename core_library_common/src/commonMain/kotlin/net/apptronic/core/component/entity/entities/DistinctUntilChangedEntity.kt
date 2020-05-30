package net.apptronic.core.component.entity.entities

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.base.RelayEntity

fun <T> Entity<T>.distinctUntilChanged(): Entity<T> {
    return DistinctUntilChangedEntity(this)
}

class DistinctUntilChangedEntity<T>(
        source: Entity<T>
) : RelayEntity<T>(source) {

    override fun proceedObserver(targetContext: Context, target: Observer<T>): Observer<T> {
        return DistinctUntilChangedObserver(target)
    }

}

private class DistinctUntilChangedObserver<T>(
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