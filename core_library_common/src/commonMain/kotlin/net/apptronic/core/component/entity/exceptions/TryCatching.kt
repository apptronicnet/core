package net.apptronic.core.component.entity.exceptions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import net.apptronic.core.component.coroutines.coroutineLauncherScoped
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.base.UpdateEntity

fun <T> tryCatching(block: () -> T): TryCatchResult<T> {
    return try {
        val result = block()
        TryCatchResult.Success(result)
    } catch (e: Exception) {
        TryCatchResult.Failure(e)
    }
}

suspend fun <T> tryCatchingSuspend(block: suspend CoroutineScope.() -> T): TryCatchResult<T> {
    return coroutineScope {
        try {
            val result = block()
            TryCatchResult.Success(result)
        } catch (e: Exception) {
            TryCatchResult.Failure<T>(e)
        }
    }
}

fun <T> TryCatchResult<T>.catchException(block: (Exception) -> Unit): TryCatchResult<T> {
    if (this is TryCatchResult.Failure) {
        block(this.exception)
    }
    return this
}

fun <T> TryCatchResult<T>.onErrorReturn(fallbackValue: T): T {
    return when (this) {
        is TryCatchResult.Success -> result
        is TryCatchResult.Failure -> fallbackValue
    }
}

fun <T> TryCatchResult<T>.onErrorReturnNull(): T? {
    return when (this) {
        is TryCatchResult.Success -> result
        is TryCatchResult.Failure -> null
    }
}

fun <T> TryCatchResult<T>.onErrorReturn(fallbackValueProvider: () -> T): T {
    return when (this) {
        is TryCatchResult.Success -> result
        is TryCatchResult.Failure -> fallbackValueProvider()
    }
}

suspend fun <T> TryCatchResult<T>.onErrorReturnSuspend(fallbackValueProvider: suspend CoroutineScope.() -> T): T {
    val tryCatchResult = this
    return coroutineScope {
        when (tryCatchResult) {
            is TryCatchResult.Success -> tryCatchResult.result
            is TryCatchResult.Failure -> fallbackValueProvider()
        }
    }
}

fun <T> TryCatchResult<T>.doFinally(block: () -> T): TryCatchResult<T> {
    block()
    return this
}

suspend fun <T> TryCatchResult<T>.doFinallySuspend(block: suspend CoroutineScope.() -> T): TryCatchResult<T> {
    coroutineScope {
        block()
    }
    return this
}

fun <T> Entity<TryCatchResult<T>>.onException(block: (Exception) -> Unit): Entity<T> {
    return OnExceptionEntity(this, block)
}

fun <T> Entity<TryCatchResult<T>>.onExceptionSuspend(block: suspend CoroutineScope.(Exception) -> Unit): Entity<T> {
    val coroutineLauncher = context.coroutineLauncherScoped()
    return OnExceptionEntity(this) { exception ->
        coroutineLauncher.launch {
            block(exception)
        }
    }
}

fun <T> Entity<TryCatchResult<T>>.sendException(handler: UpdateEntity<Exception>): Entity<T> {
    return OnExceptionEntity(this) { exception ->
        handler.update(exception)
    }
}