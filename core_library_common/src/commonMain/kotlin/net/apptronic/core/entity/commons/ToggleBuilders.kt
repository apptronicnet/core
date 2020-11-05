package net.apptronic.core.entity.commons

import net.apptronic.core.context.Contextual

fun <T> Contextual.toggle(vararg values: T): ToggleProperty<T> {
    return ToggleProperty(context, listOf(*values))
}

fun <T> Contextual.toggle(values: List<T>): ToggleProperty<T> {
    return ToggleProperty(context, values)
}

fun <T> Contextual.toggle(values: List<T>, defaultValue: T): ToggleProperty<T> {
    return ToggleProperty(context, values).apply {
        setInitValue(defaultValue)
    }
}

fun <T> Contextual.toggle(values: List<T>, defaultValueProvider: () -> T): ToggleProperty<T> {
    return ToggleProperty(context, values).apply {
        setInitValue(defaultValueProvider.invoke())
    }
}

fun Contextual.booleanToggle(defaultValue: Boolean = false): ToggleProperty<Boolean> {
    return toggle(listOf(false, true), defaultValue)
}