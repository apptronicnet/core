package net.apptronic.core.features

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import net.apptronic.core.component.context.childContext
import net.apptronic.core.component.context.close
import net.apptronic.core.component.coroutines.coroutineLaunchers
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
        awaitContext.close()
    }
}

suspend fun <T> Entity<T>.awaitUntilSuspend(condition: suspend CoroutineScope.(T) -> Boolean) {
    val awaitContext = context.childContext()
    val coroutineLauncher = awaitContext.coroutineLaunchers().local
    val deferred = CompletableDeferred<Unit>()
    val subscription = subscribe(awaitContext) {
        coroutineLauncher.launch {
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
        awaitContext.close()
    }
}