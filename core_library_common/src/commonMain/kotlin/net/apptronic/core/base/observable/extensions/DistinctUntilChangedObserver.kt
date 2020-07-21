package net.apptronic.core.base.observable.extensions

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subject.ValueHolder
import net.apptronic.core.utils.EqComparator
import net.apptronic.core.utils.SimpleEqComparator

class DistinctUntilChangedObserver<T>(
        private val target: Observer<T>,
        private val eqComparator: EqComparator<T> = SimpleEqComparator()
) : Observer<T> {

    private var lastValue: ValueHolder<T>? = null

    override fun notify(value: T) {
        val last = this.lastValue
        if (last == null || eqComparator.isEquals(last.value, value).not()) {
            this.lastValue = ValueHolder(value)
            target.notify(value)
        }
    }

}


