package net.apptronic.core.entity.commons

import net.apptronic.core.base.utils.EqComparator
import net.apptronic.core.base.utils.SimpleEqComparator
import net.apptronic.core.context.Context
import net.apptronic.core.entity.base.Value

open class BaseValue<T> internal constructor(
        context: Context,
        eqComparator: EqComparator<T> = SimpleEqComparator()
) : BaseProperty<T>(context, eqComparator), Value<T> {

    override fun set(value: T) {
        subject.update(value)
    }

    override fun update(value: T) {
        subject.update(value)
    }

    final override fun updateValue(updateCall: (T) -> T) {
        val current = get()
        val next = updateCall(current)
        update(next)
    }

}

