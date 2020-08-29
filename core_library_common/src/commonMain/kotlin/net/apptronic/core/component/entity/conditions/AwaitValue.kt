package net.apptronic.core.component.entity.conditions

import kotlinx.coroutines.CoroutineScope
import net.apptronic.core.component.entity.Entity

suspend fun <T> Entity<T>.awaitFirstValue(): T {
    val condition = createCondition()
    try {
        return condition.next()
    } finally {
        condition.release()
    }
}

suspend fun <T> Entity<T>.awaitValueMatches(matchCondition: (T) -> Boolean): T {
    val condition = createCondition()
    try {
        return condition.nextMatching(matchCondition)
    } finally {
        condition.release()
    }
}

suspend fun <T> Entity<T>.awaitValueMatchesSuspend(matchCondition: suspend CoroutineScope.(T) -> Boolean): T {
    val condition = createCondition()
    try {
        return condition.nextMatchingSuspend(matchCondition)
    } finally {
        condition.release()
    }
}