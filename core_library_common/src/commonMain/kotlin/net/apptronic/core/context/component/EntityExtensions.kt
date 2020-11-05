package net.apptronic.core.context.component

import kotlinx.coroutines.CoroutineScope
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.context.Contextual
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.entities.*
import net.apptronic.core.entity.functions.onNextSuspend

/**
 * Property of component
 */
fun <T> Contextual.value(): Value<T> {
    return Value(context)
}

/**
 * Property of view with some default value
 */
fun <T> Contextual.value(defaultValue: T): Value<T> {
    return value<T>().apply {
        set(defaultValue)
    }
}

fun <T> Contextual.valueAs(source: Entity<T>): Value<T> {
    return value<T>().apply {
        setAs(source)
    }
}

fun <T> Contextual.mutableValue(): MutableValue<T> {
    return MutableValue(context)
}

fun <T> Contextual.mutableValue(defaultValue: T): MutableValue<T> {
    return mutableValue<T>().apply {
        set(defaultValue)
    }
}

/**
 * Property of component
 */
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

fun <T> Contextual.valueSet() = mutableValue<MutableSet<T>>(mutableSetOf<T>())

fun <K, V> Contextual.valueMap() = mutableValue<MutableMap<K, V>>(mutableMapOf<K, V>())

fun <T> Contextual.valueList() = mutableValue<MutableList<T>>(mutableListOf<T>())

fun <T> Contextual.event(source: Entity<T>): Event<T> {
    return typedEvent<T>().also { event ->
        source.subscribe(context) {
            event.sendEvent(it)
        }
    }
}

/**
 * User action on screen
 */
fun Contextual.genericEvent(): GenericEvent {
    return GenericEvent(context)
}

fun Contextual.genericEvent(observer: Observer<Unit>): GenericEvent {
    return GenericEvent(context).apply {
        subscribe(observer)
    }
}

fun Contextual.genericEvent(callback: () -> Unit): GenericEvent {
    return GenericEvent(context).apply {
        subscribe {
            callback.invoke()
        }
    }
}

fun Contextual.genericEventSuspend(callback: suspend CoroutineScope.() -> Unit): GenericEvent {
    return GenericEvent(context).apply {
        onNextSuspend {
            callback()
        }
    }
}

/**
 * User action on screen
 */
fun <T> Contextual.typedEvent(): Event<T> {
    return TypedEvent(context)
}

fun <T> Contextual.typedEvent(observer: Observer<T>): Event<T> {
    return TypedEvent<T>(context).apply {
        subscribe(observer)
    }
}

fun <T> Contextual.typedEvent(callback: (T) -> Unit): Event<T> {
    return TypedEvent<T>(context).apply {
        subscribe(callback)
    }
}

fun <T> Contextual.typedEventSuspend(callback: suspend CoroutineScope.(T) -> Unit): Event<T> {
    return TypedEvent<T>(context).apply {
        onNextSuspend {
            callback(it)
        }
    }
}

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

/**
 * Create [Entity] which simply emits new item on subscribe to allow perform some transformation once.
 */
fun Contextual.newChain(): Entity<Unit> {
    return UnitEntity(context)
}

/**
 * Create [Entity] which simply emits new item on subscribe to allow perform some transformation once.
 */
fun Contextual.now(): Entity<Unit> {
    return UnitEntity(context)
}