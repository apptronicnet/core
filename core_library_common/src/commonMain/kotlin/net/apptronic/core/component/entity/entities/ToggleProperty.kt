package net.apptronic.core.component.entity.entities

import net.apptronic.core.component.context.Context

class ToggleProperty<T>(
        context: Context,
        private val values: List<T>
) : Property<T>(context) {

    private var index = -1

    internal fun setInitValue(initValue: T) {
        index = values.indexOf(initValue)
        subject.update(initValue)
    }

    fun toggle(count: Int = 1) {
        index = (index + count) % values.size
        subject.update(values[index])
    }

    fun toggleTo(value: T): Boolean {
        index = values.indexOf(value)
        return if (index >= 0) {
            subject.update(values[index])
            true
        } else {
            false
        }
    }

}