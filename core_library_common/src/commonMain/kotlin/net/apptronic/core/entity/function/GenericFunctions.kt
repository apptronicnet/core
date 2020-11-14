package net.apptronic.core.entity.function

import kotlinx.coroutines.CoroutineScope
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.FunctionProperty

fun <T, R> Entity<T>.map(map: (T) -> R): FunctionProperty<R> =
        entityFunction(this) {
            map(it)
        }

fun <T> Entity<T>.mapToString(): FunctionProperty<String> =
        map {
            it.toString()
        }

fun <T> Entity<T?>.mapToStringOrNull(): FunctionProperty<String?> =
        mapOrNull {
            it.toString()
        }

fun <T, R> Entity<T>.mapSuspend(map: suspend CoroutineScope.(T) -> R): FunctionProperty<R> =
        entityFunctionSuspend(this) {
            map(it)
        }

fun <T, R> Entity<T?>.mapOrNull(map: (T) -> R): FunctionProperty<R?> =
        map {
            if (it != null) (map.invoke(it)) else null
        }

fun <T, R> Entity<T?>.mapOrNullSuspend(map: suspend CoroutineScope.(T) -> R): FunctionProperty<R?> =
        mapSuspend {
            if (it != null) (map(it)) else null
        }

fun <T, R> Entity<T?>.mapOr(ifNull: R, map: (T) -> R): FunctionProperty<R> =
        map {
            if (it != null) (map.invoke(it)) else ifNull
        }

fun <T, R> Entity<T?>.mapOrSuspend(ifNull: R, map: suspend CoroutineScope.(T) -> R): FunctionProperty<R> =
        mapSuspend {
            if (it != null) (map(it)) else ifNull
        }

fun <T> Entity<T?>.ifNull(value: T): FunctionProperty<T> =
        entityFunction(this) {
            it ?: value
        }

fun <T> Entity<T?>.ifNull(entity: Entity<T>): FunctionProperty<T> =
        entityFunction(this, entity) { value, ifNull ->
            value ?: ifNull
        }

fun <T> Entity<T>.isTrueThat(test: (T) -> Boolean): FunctionProperty<Boolean> =
        entityFunction(this) {
            test(it)
        }

fun <T> Entity<T>.isTrueThatSuspend(test: suspend CoroutineScope.(T) -> Boolean): FunctionProperty<Boolean> =
        entityFunctionSuspend(this) {
            test(it)
        }

fun <T> Entity<T>.isFalseThat(test: (T) -> Boolean): FunctionProperty<Boolean> =
        isTrueThat(test).not()

fun <T> Entity<T>.isFalseThatSuspend(test: suspend CoroutineScope.(T) -> Boolean): FunctionProperty<Boolean> =
        isTrueThatSuspend(test).not()

fun <T> Entity<T>.toNullable(): FunctionProperty<T?> =
        entityFunction(this) {
            it
        }