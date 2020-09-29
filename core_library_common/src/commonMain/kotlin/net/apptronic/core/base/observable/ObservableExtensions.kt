package net.apptronic.core.base.observable

import net.apptronic.core.base.observable.extensions.DistinctUntilChangedObserver
import net.apptronic.core.utils.EqComparator
import net.apptronic.core.utils.SimpleEqComparator

fun <T> Observable<T>.distinctUntilChanged(eqComparator: EqComparator<T> = SimpleEqComparator()): Observable<T> {
    return DistinctUntilChangedObservable(this, eqComparator)
}

private class DistinctUntilChangedObservable<T>(
        private val source: Observable<T>,
        private val eqComparator: EqComparator<T>
) : Observable<T> {

    override fun subscribe(observer: Observer<T>): Subscription {
        return source.subscribe(DistinctUntilChangedObserver(observer, eqComparator))
    }

}
