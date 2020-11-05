package net.apptronic.core.entity.exceptions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import net.apptronic.core.context.coroutines.lifecycleCoroutineScope
import net.apptronic.core.context.coroutines.serialThrottler
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.UpdateEntity
import net.apptronic.core.entity.functions.map
import net.apptronic.core.entity.functions.mapSuspend

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

suspend fun <T> TryCatchResult<T>.catchExceptionSuspend(block: suspend CoroutineScope.(Exception) -> Unit): TryCatchResult<T> {
    val thisValue = this
    coroutineScope {
        if (thisValue is TryCatchResult.Failure) {
            block(thisValue.exception)
        }
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

fun <T> TryCatchResult<T>.onErrorProvide(fallbackValueProvider: () -> T): T {
    return when (this) {
        is TryCatchResult.Success -> result
        is TryCatchResult.Failure -> fallbackValueProvider()
    }
}

suspend fun <T> TryCatchResult<T>.onErrorProvideSuspend(fallbackValueProvider: suspend CoroutineScope.() -> T): T {
    val thisValue = this
    return coroutineScope {
        when (thisValue) {
            is TryCatchResult.Success -> thisValue.result
            is TryCatchResult.Failure -> fallbackValueProvider() as T
        }
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
    val coroutineThrottler = context.lifecycleCoroutineScope.serialThrottler()
    return OnExceptionEntity(this) { exception ->
        coroutineThrottler.launch {
            block(exception)
        }
    }
}

fun <T> Entity<TryCatchResult<T>>.sendException(handler: UpdateEntity<Exception>): Entity<T> {
    return OnExceptionEntity(this) { exception ->
        handler.update(exception)
    }
}

fun <T> Entity<TryCatchResult<T>>.mapException(mapper: (Exception) -> T): Entity<T> {
    return map {
        when (it) {
            is TryCatchResult.Success -> it.result
            is TryCatchResult.Failure -> mapper(it.exception)
        }
    }
}

fun <T> Entity<TryCatchResult<T>>.mapExceptionSuspend(mapper: suspend CoroutineScope.(Exception) -> T): Entity<T> {
    return mapSuspend {
        when (it) {
            is TryCatchResult.Success -> it.result
            is TryCatchResult.Failure -> mapper(it.exception)
        }
    }
}