package net.apptronic.core.component

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.behavior.setup
import net.apptronic.core.component.entity.entities.*
import net.apptronic.core.component.timer.Timer
import net.apptronic.core.component.timer.TimerTick

/**
 * Property of component
 */
fun <T> Component.value(): Property<T> {
    return Value(context)
}

/**
 * Property of component
 */
fun <T> Component.value(source: Entity<T>): Property<T> {
    return value<T>().setAs(source)
}

/**
 * Property of view with some default value
 */
fun <T> Component.value(defaultValue: T): Property<T> {
    return value<T>().setup {
        set(defaultValue)
    }
}

fun <T> Component.mutableValue(): MutableValue<T> {
    return MutableValue(context)
}

fun <T> Component.mutableValue(defaultValue: T): MutableValue<T> {
    return mutableValue<T>().setup {
        set(defaultValue)
    }
}

fun <T> Component.valueSet() = mutableValue<MutableSet<T>>(mutableSetOf<T>())

fun <K, V> Component.valueMap() = mutableValue<MutableMap<K, V>>(mutableMapOf<K, V>())

fun <T> Component.valueList() = mutableValue<MutableList<T>>(mutableListOf<T>())

/**
 * User action on screen
 */
fun Component.genericEvent(): GenericEvent {
    return GenericEvent(context)
}

fun Component.genericEvent(observer: Observer<Unit>): GenericEvent {
    return GenericEvent(context).apply {
        subscribe(observer)
    }
}

fun Component.genericEvent(callback: () -> Unit): GenericEvent {
    return GenericEvent(context).apply {
        subscribe {
            callback.invoke()
        }
    }
}

/**
 * User action on screen
 */
fun <T> Component.typedEvent(): Event<T> {
    return TypedEvent(context)
}

fun <T> Component.typedEvent(observer: Observer<T>): Event<T> {
    return TypedEvent<T>(context).apply {
        subscribe(observer)
    }
}

fun <T> Component.typedEvent(callback: (T) -> Unit): Event<T> {
    return TypedEvent<T>(context).apply {
        subscribe(callback)
    }
}

fun <T> Component.toggle(target: Property<T>, vararg values: T): Toggle<T> {
    return Toggle(target, *values)
}

fun Component.toggle(target: Property<Boolean>): Toggle<Boolean> {
    return Toggle(target, false, true)
}

/**
 * Create [Entity] which simply emits new item on subscribe to allow perform some transformation once.
 */
fun Component.newChain(): Entity<Unit> {
    return EmptyChain(context)
}

/**
 * Create [Entity] which simply emits new item on subscribe to allow perform some transformation once.
 */
fun Component.now(): Entity<Unit> {
    return EmptyChain(context)
}

fun <T> Component.entity(source: Entity<T>): Entity<T> {
    return value<T>().setAs(source)
}

fun <T> Component.entity(source: Entity<T>, defaultValue: T): Entity<T> {
    return value<T>(defaultValue).setAs(source)
}

fun Component.timer(
        interval: Long,
        limit: Long = Timer.INFINITE,
        action: ((TimerTick) -> Unit)? = null
): Timer {
    return Timer(this, initialInterval = interval, initialLimit = limit).also {
        if (action != null) {
            it.observe(action)
        }
    }
}