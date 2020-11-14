package net.apptronic.core.entity.commons

import kotlinx.coroutines.CoroutineScope
import net.apptronic.core.entity.Entity

fun <E : Entity<T>, T> E.onNext(action: (T) -> Unit): E {
    subscribe(action)
    return this
}

fun <E : Entity<T>, T> E.onNextSuspend(action: suspend CoroutineScope.(T) -> Unit): E {
    subscribeSuspend(action)
    return this
}
