package net.apptronic.core.base.observable

import net.apptronic.core.base.subject.ValueHolder
import net.apptronic.core.base.utils.EqComparator
import net.apptronic.core.base.utils.SimpleEqComparator

class DistinctUntilChangedObserver<T>(
        private val target: Observer<T>,
        private val eqComparator: EqComparator<T> = SimpleEqComparator()
) : Observer<T> {

    private var lastValue: ValueHolder<T>? = null

    override fun update(value: T) {
        val last = this.lastValue
        if (last == null || eqComparator.isEquals(last.value, value).not()) {
            this.lastValue = ValueHolder(value)
            target.update(value)
        }
    }

}


