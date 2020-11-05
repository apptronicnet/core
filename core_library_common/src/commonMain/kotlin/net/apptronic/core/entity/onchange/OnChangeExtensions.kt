package net.apptronic.core.entity.onchange

import kotlinx.coroutines.CoroutineScope
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.function.map
import net.apptronic.core.entity.function.mapSuspend

fun <T, E> Entity<Next<T, E>>.takeValue(): Entity<T> {
    return map { it.value }
}

fun <T, E> Entity<Next<T, E>>.takeChange(): Entity<E?> {
    return map { it.change }
}

fun <T, R, E> Entity<Next<T, E>>.mapValue(function: (T) -> R): Entity<Next<R, E>> {
    return map { next ->
        val mapped = function(next.value)
        Next(mapped, next.change)
    }
}

fun <T, R, E> Entity<Next<T, E>>.mapValueSuspend(function: suspend CoroutineScope.(T) -> R): Entity<Next<R, E>> {
    return mapSuspend { next ->
        val mapped = function(next.value)
        Next(mapped, next.change)
    }
}

fun <T, R, E> Entity<Next<T, E>>.mapChange(function: (E?) -> R?): Entity<Next<T, R>> {
    return map { next ->
        val mapped = function(next.change)
        Next(next.value, mapped)
    }
}

fun <T, R, E> Entity<Next<T, E>>.mapChangeSuspend(function: suspend CoroutineScope.(E?) -> R?): Entity<Next<T, R>> {
    return mapSuspend { next ->
        val mapped = function(next.change)
        Next(next.value, mapped)
    }
}