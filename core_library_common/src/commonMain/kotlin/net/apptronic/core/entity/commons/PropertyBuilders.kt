package net.apptronic.core.entity.commons

import net.apptronic.core.context.Contextual
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.Property

fun <T> Contextual.property(source: Entity<T>): Property<T> {
    return SourceProperty(context, source)
}

fun <T> Contextual.property(source: Entity<T>, defaultValue: T): Property<T> {
    val value = value(defaultValue).setAs(source)
    return property(value)
}

fun <T> Contextual.property(initialValue: T): Property<T> {
    val value = value(initialValue)
    return property(value)
}

