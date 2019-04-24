package net.apptronic.core.component.entity.functions.variants

import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.base.ConstantPredicate
import net.apptronic.core.component.entity.functions.predicateFunction

fun <T, R> Predicate<T>.map(map: (T) -> R): Predicate<R> =
    predicateFunction(this) {
        map(it)
    }

fun <T, R> Predicate<T?>.mapOrNull(map: (T) -> R): Predicate<R?> =
    map {
        if (it != null) (map.invoke(it)) else null
    }

fun <T, R> Predicate<T?>.mapOr(ifNull: R, map: (T) -> R): Predicate<R> =
    map {
        if (it != null) (map.invoke(it)) else ifNull
    }

fun <T> Predicate<T?>.ifNull(value: T): Predicate<T> =
    predicateFunction(this) {
        it ?: value
    }

fun <T> Predicate<T?>.ifNull(predicate: Predicate<T>): Predicate<T> =
    predicateFunction(this, predicate) { value, ifNull ->
        value ?: ifNull
    }

fun <T> nullValue(): Predicate<T?> =
    ConstantPredicate<T?>(null)

fun <T> ofValue(value: T): Predicate<T> =
    ConstantPredicate(value)

fun <T> T.asValue(): Predicate<T> =
    ConstantPredicate(this)

fun <T> Predicate<T>.isTrueThat(test: T.() -> Boolean): Predicate<Boolean> =
    predicateFunction(this) {
        it.test()
    }

fun <T> Predicate<T>.isFalseThat(test: T.() -> Boolean): Predicate<Boolean> =
    isTrueThat(test).not()

fun <T> Predicate<T>.toNullable(): Predicate<T?> =
    predicateFunction(this) {
        it
    }