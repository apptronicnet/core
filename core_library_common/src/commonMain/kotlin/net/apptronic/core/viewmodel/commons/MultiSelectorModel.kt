package net.apptronic.core.viewmodel.commons

import net.apptronic.core.base.utils.SetEqComparator
import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.entity.commons.*

fun <T> Contextual.multiSelector(): MultiSelectorModel<T> {
    return MultiSelectorModel<T>(context).apply {
        set(emptySet<T>())
    }
}

fun <T> Contextual.multiSelector(initialSelection: Collection<T>): MultiSelectorModel<T> {
    return MultiSelectorModel<T>(context).apply {
        set(initialSelection.toSet())
    }
}

/**
 * Allows to select multiple value of type [T] using [SwitchModel]s provided by [getSwitch]
 */
class MultiSelectorModel<T> internal constructor(
        context: Context
) : MutableEntity<Set<T>> by Value<Set<T>>(context, SetEqComparator<T>()), SwitchableSelector<T> {

    init {
        set(emptySet())
    }

    fun updateSelection(selection: T, isSelected: Boolean) {
        if (isSelected) {
            update(withAdded(selection))
        } else {
            update(withRemoved(selection))
        }
    }

    fun addSelection(vararg selection: T) {
        val current = get()
        val next = mutableSetOf<T>().apply {
            addAll(current)
            addAll(selection)
        }
        update(next)
    }

    fun removeSelection(vararg selection: T) {
        val current = get()
        val next = current.filterNot { selection.contains(it) }.toSet()
        update(next)
    }

    fun set(vararg values: T) {
        set(values.toSet())
    }

    fun update(vararg values: T) {
        update(values.toSet())
    }

    private fun withAdded(value: T): Set<T> {
        return mutableSetOf<T>().apply {
            addAll(get())
            add(value)
        }
    }

    private fun withRemoved(value: T): Set<T> {
        return get().filter { it != value }.toSet()
    }

    override fun getSwitch(selection: T): MutableEntity<Boolean> {
        val switchModel = context.value(get().contains(selection))
        switchModel.notifications.subscribe {
            if (it.value) {
                mutateUsingNotification(withAdded(selection), it.isUpdate)
            } else {
                mutateUsingNotification(withRemoved(selection), it.isUpdate)
            }
        }
        notifications.mapNotification {
            it.contains(selection)
        }.subscribe {
            switchModel.mutateUsingNotification(it)
        }
        return switchModel
    }

}