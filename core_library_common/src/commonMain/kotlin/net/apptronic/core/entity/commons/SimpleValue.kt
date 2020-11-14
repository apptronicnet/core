package net.apptronic.core.entity.commons

import net.apptronic.core.base.utils.EqComparator
import net.apptronic.core.base.utils.SimpleEqComparator
import net.apptronic.core.context.Context
import net.apptronic.core.entity.base.Value

class SimpleValue<T> internal constructor(
        context: Context,
        eqComparator: EqComparator<T> = SimpleEqComparator()
) : SimpleProperty<T>(context, eqComparator), Value<T> {


    override fun set(value: T) {
        subject.update(value)
    }

}

