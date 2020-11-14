package net.apptronic.core.viewmodel.commons

import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.entity.base.MutableValue
import net.apptronic.core.entity.commons.BaseMutableValue
import net.apptronic.core.entity.commons.mutableValue
import net.apptronic.core.entity.reflection.reflect

fun <T, Id> Contextual.listSelector(getId: (T) -> Id): ListSelectorModel<T, Id> {
    return ListSelectorModel(context, getId)
}

fun <T> Contextual.listSelector(): ListSelectorModel<T, T> {
    return ListSelectorModel(context, { it })
}

/**
 * Model for creating list selectors when it needed to choose items from list
 */
class ListSelectorModel<T, Id> internal constructor(
        context: Context,
        private val getId: (T) -> Id,
        private val value: MutableValue<T?> = BaseMutableValue<T?>(context)
) : MutableValue<T?> by value {

    private val T?.id: Id?
        get() = if (this != null) getId(this) else null

    private val Id?.item: T?
        get() = items.getOr(emptyList()).firstOrNull {
            getId(it) == this
        }

    val items: MutableValue<List<T>> = context.mutableValue()
    val id: MutableValue<Id?> = reflect(direct = { it.id }, reverse = { it.item })

    init {
        items.set(emptyList())
        set(null)
        id.set(null)
        items.subscribe {
            val current = this.getOrNull()
            if (!it.contains(current)) {
                update(null)
            }
        }
    }

    override fun set(value: T?) {
        if (items.get().contains(value)) {
            this.value.set(value)
        } else {
            this.value.set(null)
        }
    }

    override fun update(value: T?) {
        if (items.get().contains(value)) {
            this.value.update(value)
        } else {
            this.value.update(null)
        }
    }

}