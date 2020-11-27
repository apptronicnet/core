package net.apptronic.core.entity.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.apptronic.core.context.coroutines.contextCoroutineScope
import net.apptronic.core.entity.Entity

fun <E : Entity<T>, T> E.withOnNext(action: (T) -> Unit): E {
    subscribe(action)
    return this
}

fun <E : Entity<T>, T> E.withOnNextSuspend(action: suspend CoroutineScope.(T) -> Unit): E {
    subscribeSuspend(action)
    return this
}

fun <E : Value<T>, T> E.withStateFrom(source: Entity<T>): E {
    source.subscribe {
        set(it)
    }
    return this
}

fun <E : Value<T>, T> E.withLoadState(action: () -> T): E {
    set(action())
    return this
}

fun <E : Value<T>, T> E.withLoadStateSuspend(action: suspend CoroutineScope.() -> T): E {
    context.contextCoroutineScope.launch {
        set(action())
    }
    return this
}

fun <E : MutableValue<T>, T> E.withOnUpdate(action: (T) -> Unit): E {
    updates.subscribe(action)
    return this
}

fun <E : MutableValue<T>, T> E.withOnUpdateSuspend(action: suspend CoroutineScope.(T) -> Unit): E {
    updates.subscribeSuspend(action)
    return this
}

fun <E : MutableValue<T>, T> E.withOnUpdate(target: SubjectEntity<T>): E {
    updates.subscribe(target)
    return this
}