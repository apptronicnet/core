package net.apptronic.core.entity.commons

import net.apptronic.core.context.Contextual
import net.apptronic.core.entity.Entity

fun <T> Contextual.property(source: Entity<T>): BaseProperty<T> {
    return SourceProperty(context, source)
}

fun <T> Contextual.property(source: Entity<T>, defaultValue: T): BaseProperty<T> {
    val value = value(defaultValue).setAs(source)
    return property(value)
}

fun <T> Contextual.property(initialValue: T): BaseProperty<T> {
    val value = value(initialValue)
    return property(value)
}

