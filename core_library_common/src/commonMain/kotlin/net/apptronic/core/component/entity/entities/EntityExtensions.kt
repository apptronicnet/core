package net.apptronic.core.component.entity.entities

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.base.EntityValue
import net.apptronic.core.component.entity.base.UpdateEntity
import net.apptronic.core.component.entity.functions.toNullable
import net.apptronic.core.component.entity.subscribe

/**
 * Subscribe to updates of [source] and set all new values automatically
 */
fun <E : UpdateEntity<T>, T> E.setAs(source: Entity<out T>): E {
    source.subscribe(context) {
        update(it)
    }
    return this
}

/**
 * Subscribe to updates of [source] and set all new values automatically
 */
fun <T> Entity<T>.setTo(target: UpdateEntity<T>): Entity<T> {
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
        result.send(Unit)
    }
    return result
}

fun <T> Entity<T>.withDefault(defaultValue: T): EntityValue<T> {
    return Value<T>(context).also {
        it.set(defaultValue)
        it.setAs(this)
    }
}

fun <T> Entity<T>.withDefaultNull(): EntityValue<T?> {
    return Value<T?>(context).also {
        it.set(null)
        it.setAs(this.toNullable())
    }
}


