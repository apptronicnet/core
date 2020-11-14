package net.apptronic.core.entity.commons

import net.apptronic.core.context.Context
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.Property
import net.apptronic.core.entity.base.Value
import net.apptronic.core.entity.function.toNullable

/**
 * Subscribe to updates of [source] and set all new values automatically
 */
fun <E : Value<T>, T> E.setAs(source: Entity<out T>): E {
    source.subscribe(context) {
        set(it)
    }
    return this
}

/**
 * Subscribe to updates of [source] and set all new values automatically
 */
fun <T> Entity<T>.setTo(target: Value<T>): Entity<T> {
    target.setAs(this)
    return this
}

fun <T> Entity<T>.asProperty(): Property<T> {
    return SourceProperty(context, this)
}

fun <T> Entity<T>.asEvent(): Event<T> {
    val event = TypedEvent<T>(context)
    this.subscribe(event)
    return event
}

fun Entity<*>.mirrorGenericEvent(context: Context): Event<Unit> {
    val result = TypedEvent<Unit>(context)
    subscribe(context) {
        result.update(Unit)
    }
    return result
}

fun <T> Entity<T>.withDefault(defaultValue: T): Property<T> {
    return SimpleMutableValue<T>(context).also {
        it.set(defaultValue)
        it.setAs(this)
    }
}

fun <T> Entity<T>.withDefaultNull(): Property<T?> {
    return SimpleMutableValue<T?>(context).also {
        it.set(null)
        it.setAs(this.toNullable())
    }
}


