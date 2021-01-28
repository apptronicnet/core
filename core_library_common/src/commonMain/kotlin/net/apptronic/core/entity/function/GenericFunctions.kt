package net.apptronic.core.entity.function

import kotlinx.coroutines.CoroutineScope
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.FunctionProperty

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