package net.apptronic.core.component.entity.conditions

import kotlinx.coroutines.CoroutineScope
import net.apptronic.core.component.entity.Entity

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

suspend fun <T> Entity<T>.awaitUntilValue(value: T) {
    awaitUntilCondition<T> {
        it == value
    }
}

suspend fun Entity<Boolean>.awaitUntilTrue() {
    awaitUntilValue(true)
}

suspend fun Entity<Boolean>.awaitUntilFalse() {
    awaitUntilValue(false)
}

suspend fun <T> Entity<T>.awaitUntilSet() {
    val condition = createCondition()
    try {
        condition.awaitAny()
    } finally {
        condition.release()
    }
}