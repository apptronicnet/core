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

    fun toggle() {
        index = (index + 1) % values.size
        subject.update(values[index])
    }

}