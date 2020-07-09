package net.apptronic.core.component

import kotlinx.coroutines.CoroutineScope
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.behavior.setup
import net.apptronic.core.component.entity.entities.*
import net.apptronic.core.component.entity.functions.onNextSuspend
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.component.timer.Timer
import net.apptronic.core.component.timer.TimerTick

/**
 * Property of component
 */
fun <T> Component.value(): Value<T> {
    return Value(context)
}

/**
 * Property of view with some default value
 */
fun <T> Component.value(defaultValue: T): Value<T> {
    return value<T>().setup {
        set(defaultValue)
    }
}

fun <T> Component.valueAs(source: Entity<T>): Value<T> {
    return value<T>().apply {
        setAs(source)
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

/**
 * Property of component
 */
fun <T> Component.property(source: Entity<T>): Property<T> {
    return SourceProperty(context, source)
}

fun <T> Component.property(source: Entity<T>, defaultValue: T): Property<T> {
    val value = value(defaultValue).setAs(source)
    return property(value)
}

fun <T> Component.property(initialValue: T): Property<T> {
    val value = value(initialValue)
    return property(value)
}

fun <T> Component.valueSet() = mutableValue<MutableSet<T>>(mutableSetOf<T>())

fun <K, V> Component.valueMap() = mutableValue<MutableMap<K, V>>(mutableMapOf<K, V>())

fun <T> Component.valueList() = mutableValue<MutableList<T>>(mutableListOf<T>())

fun <T> Component.event(source: Entity<T>): Event<T> {
    return typedEvent<T>().also { event ->
        source.subscribe(context) {
            event.sendEvent(it)
        }
    }
}

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

fun Component.genericEventSuspend(callback: suspend CoroutineScope.() -> Unit): GenericEvent {
    return GenericEvent(context).apply {
        onNextSuspend {
            callback()
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

fun <T> Component.typedEventSuspend(callback: suspend CoroutineScope.(T) -> Unit): Event<T> {
    return TypedEvent<T>(context).apply {
        onNextSuspend {
            callback(it)
        }
    }
}

fun <T> Component.toggle(vararg values: T): ToggleProperty<T> {
    return ToggleProperty(context, listOf(*values))
}

fun <T> Component.toggle(values: List<T>): ToggleProperty<T> {
    return ToggleProperty(context, values)
}

fun <T> Component.toggle(values: List<T>, defaultValue: T): ToggleProperty<T> {
    return ToggleProperty(context, values).apply {
        setInitValue(defaultValue)
    }
}

fun <T> Component.toggle(values: List<T>, defaultValueProvider: () -> T): ToggleProperty<T> {
    return ToggleProperty(context, values).apply {
        setInitValue(defaultValueProvider.invoke())
    }
}

fun Component.booleanToggle(defaultValue: Boolean = false): ToggleProperty<Boolean> {
    return toggle(listOf(false, true), defaultValue)
}

/**
 * Create [Entity] which simply emits new item on subscribe to allow perform some transformation once.
 */
fun Component.newChain(): Entity<Unit> {
    return UnitEntity(context)
}

/**
 * Create [Entity] which simply emits new item on subscribe to allow perform some transformation once.
 */
fun Component.now(): Entity<Unit> {
    return UnitEntity(context)
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