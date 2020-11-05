package net.apptronic.core.entity.behavior

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.RelayEntity

fun <T> Entity<T>.takeWhen(chooser: Entity<Boolean>, whileNotSet: Boolean = false): Entity<T> {
    return TakeWhenEntity(this, chooser, whileNotSet)
}

private class TakeWhenEntity<T>(
        source: Entity<T>,
        chooser: Entity<Boolean>,
        whileNotSet: Boolean = false
) : RelayEntity<T>(source) {

    private var isShouldTake = whileNotSet

    init {
        chooser.subscribe(context) {
            isShouldTake = it
        }
    }

    override fun onNext(nextValue: T, observer: Observer<T>) {
        if (isShouldTake) {
            observer.notify(nextValue)
        }
    }

}