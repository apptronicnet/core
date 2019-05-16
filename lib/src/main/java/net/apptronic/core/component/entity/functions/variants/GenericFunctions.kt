package net.apptronic.core.component.entity.functions.variants

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.base.ConstantEntity
import net.apptronic.core.component.entity.functions.entityFunction

fun <T> Entity<T>.onNext(action: (T) -> Unit): Entity<T> =
    map {
        action.invoke(it)
        it
    }

fun <T, R> Entity<T>.map(map: (T) -> R): Entity<R> =
    entityFunction(this) {
        map(it)
    }

fun <T, R> Entity<T?>.mapOrNull(map: (T) -> R): Entity<R?> =
    map {
        if (it != null) (map.invoke(it)) else null
    }

fun <T, R> Entity<T?>.mapOr(ifNull: R, map: (T) -> R): Entity<R> =
    map {
        if (it != null) (map.invoke(it)) else ifNull
    }

fun <T> Entity<T?>.ifNull(value: T): Entity<T> =
    entityFunction(this) {
        it ?: value
    }

fun <T> Entity<T?>.ifNull(entity: Entity<T>): Entity<T> =
    entityFunction(this, entity) { value, ifNull ->
        value ?: ifNull
    }

fun <T> Context.nullValue(): Entity<T?> =
    ConstantEntity<T?>(this, null)

fun <T> Context.ofValue(value: T): Entity<T> =
    ConstantEntity(this, value)

fun <T> Entity<T>.isTrueThat(test: T.() -> Boolean): Entity<Boolean> =
    entityFunction(this) {
        it.test()
    }

fun <T> Entity<T>.isFalseThat(test: T.() -> Boolean): Entity<Boolean> =
    isTrueThat(test).not()

fun <T> Entity<T>.toNullable(): Entity<T?> =
    entityFunction(this) {
        it
    }