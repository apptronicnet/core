package net.apptronic.core.entity.onchange

import net.apptronic.core.context.Contextual
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.Property
import net.apptronic.core.entity.commons.setAs

fun <T, E> Entity<Next<T, E>>.asOnChangeProperty(): OnChangeProperty<T, E> {
    return OnChangeImpl<T, E>(context).setAs(this)
}

fun <T, E> Contextual.onChangeProperty(source: Entity<Next<T, E>>): OnChangeProperty<T, E> {
    return onChangeValue<T, E>().setAs(source)
}

fun <T, E> Contextual.onChangeProperty(initValue: T): OnChangeProperty<T, E> {
    return onChangeValue(initValue)
}

fun <T, E> Contextual.onChangeValue(): OnChangeValue<T, E> {
    return OnChangeImpl<T, E>(context)
}

fun <T, E> Contextual.onChangeValue(initValue: T): OnChangeValue<T, E> {
    return OnChangeImpl<T, E>(context).apply {
        set(initValue)
    }
}

interface OnChangeProperty<T, E> : Property<Next<T, E>> {

    fun getValue(): T

    fun getValueOrNull(): T?

    fun doIfValueSet(action: (T) -> Unit): Boolean

    fun getValueOr(fallbackValue: T): T

    fun getValueOr(fallbackValueProvider: () -> T): T

}

