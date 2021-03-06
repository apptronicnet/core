package net.apptronic.core.viewmodel.commons

import net.apptronic.core.base.utils.SetEqComparator
import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.entity.base.MutableValue
import net.apptronic.core.entity.base.mapChange
import net.apptronic.core.entity.commons.BaseMutableValue
import net.apptronic.core.entity.commons.mutableValue

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
) : MutableValue<Set<T>> by BaseMutableValue<Set<T>>(context, SetEqComparator<T>()), SwitchableSelector<T> {

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

    override fun getSwitch(selection: T): MutableValue<Boolean> {
        val switchModel = context.mutableValue(get().contains(selection))
        switchModel.changes.subscribe {
            if (it.value) {
                applyChange(withAdded(selection), it.isUpdate)
            } else {
                applyChange(withRemoved(selection), it.isUpdate)
            }
        }
        changes.mapChange {
            it.contains(selection)
        }.subscribe {
            switchModel.applyChange(it)
        }
        return switchModel
    }

}