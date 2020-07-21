package net.apptronic.core.component.entity.entities

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.extensions.DistinctUntilChangedObserver
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.base.RelayEntity
import net.apptronic.core.utils.EqComparator
import net.apptronic.core.utils.SimpleEqComparator

fun <T> Entity<T>.distinctUntilChanged(eqComparator: EqComparator<T> = SimpleEqComparator()): Entity<T> {
    return DistinctUntilChangedEntity(this, eqComparator)
}

private class DistinctUntilChangedEntity<T>(
        source: Entity<T>,
        private val eqComparator: EqComparator<T>
) : RelayEntity<T>(source) {

    override fun onNewObserver(targetContext: Context, observer: Observer<T>): Observer<T> {
        return DistinctUntilChangedObserver(observer, eqComparator)
    }

}