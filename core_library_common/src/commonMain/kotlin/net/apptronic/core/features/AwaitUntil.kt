package net.apptronic.core.features

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.apptronic.core.component.context.childContext
import net.apptronic.core.component.context.terminate
import net.apptronic.core.component.coroutines.contextCoroutineScope
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.subscribe

suspend fun <T> Entity<T>.awaitUntil(condition: (T) -> Boolean) {
    val awaitContext = context.childContext()
    val deferred = CompletableDeferred<Unit>()
    val subscription = subscribe(awaitContext) {
        try {
            if (condition(it)) {
                deferred.complete(Unit)
            }
        } catch (e: Exception) {
            deferred.completeExceptionally(e)
        }
    }
    try {
        deferred.await()
    } finally {
        subscription.unsubscribe()
        awaitContext.terminate()
    }
}

suspend fun <T> Entity<T>.awaitUntil(value: T) {
    awaitUntil<T> {
        it == value
    }
}

suspend fun Entity<Boolean>.awaitUntilTrue() {
    awaitUntil(true)
}

suspend fun Entity<Boolean>.awaitUntilFalse() {
    awaitUntil(false)
}

suspend fun <T> Entity<T>.awaitUntilSuspend(condition: suspend CoroutineScope.(T) -> Boolean) {
    val awaitContext = context.childContext()
    val coroutineScope = awaitContext.contextCoroutineScope
    val deferred = CompletableDeferred<Unit>()
    val subscription = subscribe(awaitContext) {
        coroutineScope.launch {
            try {
                if (condition(it)) {
                    deferred.complete(Unit)
                }
            } catch (e: Exception) {
                deferred.completeExceptionally(e)
            }
        }
    }
    try {
        deferred.await()
    } finally {
        subscription.unsubscribe()
        awaitContext.terminate()
    }
}