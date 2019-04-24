package net.apptronic.core.component.entity.entities

import net.apptronic.core.component.entity.base.ValueHolder
import net.apptronic.core.component.entity.subscribe

class Toggle<T>(
    private val target: Property<T>,
    private vararg val values: T
) {

    private var value: ValueHolder<T>? = null

    init {
        target.subscribe {
            value = ValueHolder(it)
        }
    }

    fun toggle() {
        val index = value?.let { values.indexOf(it.value) } ?: -1
        val next = values[(index + 1) % values.size]
        target.set(next)
    }

}