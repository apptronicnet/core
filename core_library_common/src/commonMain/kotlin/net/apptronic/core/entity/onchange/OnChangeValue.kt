package net.apptronic.core.entity.onchange

import net.apptronic.core.entity.base.Value

interface OnChangeValue<T, E> : OnChangeProperty<T, E>, Value<Next<T, E>> {

    fun set(next: T, change: E? = null)

    fun update(next: T, change: E? = null)

    fun getValueEntity(): Value<T>

}

