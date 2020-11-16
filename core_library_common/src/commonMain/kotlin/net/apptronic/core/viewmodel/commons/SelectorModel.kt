package net.apptronic.core.viewmodel.commons

import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.entity.base.MutableValue
import net.apptronic.core.entity.base.mapChange
import net.apptronic.core.entity.commons.mutableValue

fun <T> Contextual.selector(): SelectorModel<T> {
    return SelectorModel<T>(context, true).apply {
        set(null)
    }
}

fun <T> Contextual.selector(defaultValue: T?, supportsUnselect: Boolean = true): SelectorModel<T> {
    return SelectorModel<T>(context, supportsUnselect).apply {
        set(defaultValue)
    }
}

/**
 * Allows to select single value of type [T] using [SwitchModel]s provided by [getSwitch]
 */
class SelectorModel<T> internal constructor(
        context: Context,
        private val supportsUnselect: Boolean,
        private val selection: MutableValue<T?> = context.mutableValue()
) : MutableValue<T?> by selection, SwitchableSelector<T> {

    private inner class Switch(val selection: T, val holder: MutableValue<Boolean>) : MutableValue<Boolean> by holder {

        override fun set(value: Boolean) {
            setSelectionInternal(selection, value, false)
        }

        override fun update(value: Boolean) {
            setSelectionInternal(selection, value, true)
        }

        private fun setSelectionInternal(selection: T, isSelected: Boolean, isUpdate: Boolean) {
            if (isSelected) {
                this@SelectorModel.applyChange(selection, isUpdate)
                holder.applyChange(true, isUpdate)
            } else {
                val currentSelection = this@SelectorModel.get()
                if (currentSelection == selection) {
                    if (currentSelection == selection && supportsUnselect) {
                        holder.applyChange(false, isUpdate)
                        this@SelectorModel.applyChange(null, isUpdate)
                    } else {
                        // state should be changed but immediately reverted
                        // for UI: in this case UI will receive notification that state is changed
                        holder.set(false)
                        holder.set(true)
                    }
                }
            }
        }

    }

    override fun getSwitch(selection: T): MutableValue<Boolean> {
        val value = context.mutableValue(get() == selection)
        val switch = Switch(selection, value)
        changes.mapChange {
            it == selection
        }.subscribe {
            value.applyChange(it)
        }
        return switch
    }

}