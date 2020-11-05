package net.apptronic.core.entity.commons

import net.apptronic.core.base.observable.DistinctUntilChangedObserver
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.utils.EqComparator
import net.apptronic.core.base.utils.SimpleEqComparator
import net.apptronic.core.context.Context
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.RelayEntity

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