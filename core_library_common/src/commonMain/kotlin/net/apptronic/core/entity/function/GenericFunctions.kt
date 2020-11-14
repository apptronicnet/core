package net.apptronic.core.entity.function

import kotlinx.coroutines.CoroutineScope
import net.apptronic.core.context.component.IComponent
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.Property
import net.apptronic.core.entity.commons.ConstantEntity

fun <T, R> Entity<T>.map(map: (T) -> R): Property<R> =
        entityFunction(this) {
            map(it)
        }

fun <T> Entity<T>.mapToString(): Property<String> =
        map {
            it.toString()
        }

fun <T> Entity<T?>.mapToStringOrNull(): Property<String?> =
        mapOrNull {
            it.toString()
        }

fun <T, R> Entity<T>.mapSuspend(map: suspend CoroutineScope.(T) -> R): Property<R> =
        entityFunctionSuspend(this) {
            map(it)
        }

fun <T, R> Entity<T?>.mapOrNull(map: (T) -> R): Property<R?> =
        map {
            if (it != null) (map.invoke(it)) else null
        }

fun <T, R> Entity<T?>.mapOrNullSuspend(map: suspend CoroutineScope.(T) -> R): Property<R?> =
        mapSuspend {
            if (it != null) (map(it)) else null
        }

fun <T, R> Entity<T?>.mapOr(ifNull: R, map: (T) -> R): Property<R> =
        map {
            if (it != null) (map.invoke(it)) else ifNull
        }

fun <T, R> Entity<T?>.mapOrSuspend(ifNull: R, map: suspend CoroutineScope.(T) -> R): Property<R> =
        mapSuspend {
            if (it != null) (map(it)) else ifNull
        }

fun <T> Entity<T?>.ifNull(value: T): Property<T> =
        entityFunction(this) {
            it ?: value
        }

fun <T> Entity<T?>.ifNull(entity: Entity<T>): Property<T> =
        entityFunction(this, entity) { value, ifNull ->
            value ?: ifNull
        }

fun <T> IComponent.nullValue(): Property<T?> =
        ConstantEntity<T?>(context, null)

fun <T> IComponent.ofValue(value: T): Property<T> =
        ConstantEntity(context, value)

fun <T> Entity<T>.isTrueThat(test: (T) -> Boolean): Property<Boolean> =
        entityFunction(this) {
            test(it)
        }

fun <T> Entity<T>.isTrueThatSuspend(test: suspend CoroutineScope.(T) -> Boolean): Property<Boolean> =
        entityFunctionSuspend(this) {
            test(it)
        }

fun <T> Entity<T>.isFalseThat(test: (T) -> Boolean): Property<Boolean> =
        isTrueThat(test).not()

fun <T> Entity<T>.isFalseThatSuspend(test: suspend CoroutineScope.(T) -> Boolean): Property<Boolean> =
        isTrueThatSuspend(test).not()

fun <T> Entity<T>.toNullable(): Property<T?> =
        entityFunction(this) {
            it
        }