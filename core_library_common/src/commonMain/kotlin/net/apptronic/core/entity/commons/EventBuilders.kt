package net.apptronic.core.entity.commons

import kotlinx.coroutines.CoroutineScope
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.context.Contextual
import net.apptronic.core.entity.Entity

fun <T> Contextual.event(source: Entity<T>): Event<T> {
    return typedEvent<T>().also { event ->
        source.subscribe(context) {
            event.update(it)
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
        subscribeSuspend {
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
        subscribeSuspend {
            callback(it)
        }
    }
}
