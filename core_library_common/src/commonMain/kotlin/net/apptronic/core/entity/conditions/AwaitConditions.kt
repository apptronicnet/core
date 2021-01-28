package net.apptronic.core.entity.conditions

import kotlinx.coroutines.CoroutineScope
import net.apptronic.core.entity.Entity

suspend fun <T> Entity<T>.awaitAny(): T {
    val condition = createCondition()
    try {
        return condition.next()
    } finally {
        condition.release()
    }
}

suspend fun <T> Entity<T>.awaitMatches(matchCondition: (T) -> Boolean): T {
    val condition = createCondition()
    try {
        return condition.nextMatching(matchCondition)
    } finally {
        condition.release()
    }
}

suspend fun <T> Entity<T>.awaitMatchesSuspend(matchCondition: suspend CoroutineScope.(T) -> Boolean): T {
    val condition = createCondition()
    try {
        return condition.nextMatchingSuspend(matchCondition)
    } finally {
        condition.release()
    }
}

suspend fun <T> Entity<T>.awaitUntilCondition(awaitCondition: (T) -> Boolean) {
    val condition = createCondition()
    try {
        condition.awaitMatching(awaitCondition)
    } finally {
        condition.release()
    }
}

suspend fun <T> Entity<T>.awaitUntilConditionSuspend(awaitCondition: suspend CoroutineScope.(T) -> Boolean) {
    val condition = createCondition()
    try {
        condition.awaitMatchingSuspend(awaitCondition)
    } finally {
        condition.release()
    }
}

suspend fun <T> Entity<T>.awaitUntilMatches(value: T) {
    awaitUntilCondition<T> {
        it == value
    }
}

suspend fun Entity<Boolean>.awaitUntilTrue() {
    awaitUntilMatches(true)
}

suspend fun Entity<Boolean>.awaitUntilFalse() {
    awaitUntilMatches(false)
}

suspend fun <T> Entity<T>.awaitUntilSet() {
    val condition = createCondition()
    try {
        condition.awaitAny()
    } finally {
        condition.release()
    }
}