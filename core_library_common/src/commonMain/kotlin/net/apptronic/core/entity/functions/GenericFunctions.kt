package net.apptronic.core.entity.functions

import kotlinx.coroutines.CoroutineScope
import net.apptronic.core.context.component.IComponent
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.ConstantEntity

fun <T> Entity<T>.onNext(action: (T) -> Unit): Entity<T> {
    subscribe(action)
    return this
}

fun <T> Entity<T>.onNextSuspend(action: suspend CoroutineScope.(T) -> Unit): Entity<T> {
    subscribeSuspend(action)
    return this
}

fun <T, R> Entity<T>.map(map: (T) -> R): Entity<R> =
        entityFunction(this) {
            map(it)
        }

fun <T> Entity<T>.mapToString(): Entity<String> =
        map {
            it.toString()
        }

fun <T> Entity<T?>.mapToStringOrNull(): Entity<String?> =
        mapOrNull {
            it.toString()
        }

fun <T, R> Entity<T>.mapSuspend(map: suspend CoroutineScope.(T) -> R): Entity<R> =
        entityFunctionSuspend(this) {
            map(it)
        }

fun <T, R> Entity<T?>.mapOrNull(map: (T) -> R): Entity<R?> =
        map {
            if (it != null) (map.invoke(it)) else null
        }

fun <T, R> Entity<T?>.mapOrNullSuspend(map: suspend CoroutineScope.(T) -> R): Entity<R?> =
        mapSuspend {
            if (it != null) (map(it)) else null
        }

fun <T, R> Entity<T?>.mapOr(ifNull: R, map: (T) -> R): Entity<R> =
        map {
            if (it != null) (map.invoke(it)) else ifNull
        }

fun <T, R> Entity<T?>.mapOrSuspend(ifNull: R, map: suspend CoroutineScope.(T) -> R): Entity<R> =
        mapSuspend {
            if (it != null) (map(it)) else ifNull
        }

fun <T> Entity<T?>.ifNull(value: T): Entity<T> =
        entityFunction(this) {
            it ?: value
        }

fun <T> Entity<T?>.ifNull(entity: Entity<T>): Entity<T> =
        entityFunction(this, entity) { value, ifNull ->
            value ?: ifNull
        }

fun <T> IComponent.nullValue(): Entity<T?> =
        ConstantEntity<T?>(context, null)

fun <T> IComponent.ofValue(value: T): Entity<T> =
        ConstantEntity(context, value)

fun <T> Entity<T>.isTrueThat(test: (T) -> Boolean): Entity<Boolean> =
        entityFunction(this) {
            test(it)
        }

fun <T> Entity<T>.isTrueThatSuspend(test: suspend CoroutineScope.(T) -> Boolean): Entity<Boolean> =
        entityFunctionSuspend(this) {
            test(it)
        }

fun <T> Entity<T>.isFalseThat(test: (T) -> Boolean): Entity<Boolean> =
        isTrueThat(test).not()

fun <T> Entity<T>.isFalseThatSuspend(test: suspend CoroutineScope.(T) -> Boolean): Entity<Boolean> =
        isTrueThatSuspend(test).not()

fun <T> Entity<T>.toNullable(): Entity<T?> =
        entityFunction(this) {
            it
        }