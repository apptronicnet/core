package net.apptronic.core.viewmodel.commons

import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.entity.commons.*

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
        private val selection: Value<T?> = context.value<T?>()
) : MutableEntity<T?> by selection, SwitchableSelector<T> {

    private val reselectEvent = context.typedEvent<T>()

    private fun setSelectionInternal(selection: T, isSelected: Boolean, isUpdate: Boolean) {
        if (isSelected) {
            mutateUsingNotification(selection, isUpdate)
        } else {
            if (get() == selection) {
                if (get() == selection && supportsUnselect) {
                    mutateUsingNotification(null, isUpdate)
                } else {
                    reselectEvent.sendEvent(selection)
                }
            }
        }
    }

    private inner class Switch(val selection: T, val holder: MutableEntity<Boolean>) : MutableEntity<Boolean> by holder {

        override fun set(value: Boolean) {
            setSelectionInternal(selection, value, false)
        }

        override fun update(value: Boolean) {
            setSelectionInternal(selection, value, true)
        }

        private fun setSelectionInternal(selection: T, isSelected: Boolean, isUpdate: Boolean) {
            if (isSelected) {
                this@SelectorModel.mutateUsingNotification(selection, isUpdate)
                holder.mutateUsingNotification(true, isUpdate)
            } else {
                val currentSelection = this@SelectorModel.get()
                if (currentSelection == selection) {
                    if (currentSelection == selection && supportsUnselect) {
                        holder.mutateUsingNotification(false, isUpdate)
                        this@SelectorModel.mutateUsingNotification(null, isUpdate)
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

    override fun getSwitch(selection: T): MutableEntity<Boolean> {
        val value = context.value(get() == selection)
        val switch = Switch(selection, value)
        value.notifications.subscribe {
            setSelectionInternal(selection, it.value, it.isUpdate)
        }
        notifications.mapNotification {
            it == selection
        }.subscribe {
            value.mutateUsingNotification(it)
        }
        return switch
    }

}