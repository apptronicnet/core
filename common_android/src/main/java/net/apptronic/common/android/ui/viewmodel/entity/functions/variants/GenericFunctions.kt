package net.apptronic.common.android.ui.viewmodel.entity.functions.variants

import net.apptronic.common.android.ui.viewmodel.entity.functions.Predicate
import net.apptronic.common.android.ui.viewmodel.entity.functions.ValuePredicate
import net.apptronic.common.android.ui.viewmodel.entity.functions.predicateFunction

fun <T, R> Predicate<T>.map(map: T.() -> R): Predicate<R> =
    predicateFunction(this) {
        it.map()
    }

fun <T> Predicate<T?>.ifNull(value: T): Predicate<T> =
    predicateFunction(this) {
        it ?: value
    }

fun <T> Predicate<T?>.ifNull(predicate: Predicate<T>): Predicate<T> =
    predicateFunction(this, predicate) { value, ifNull ->
        value ?: ifNull
    }

fun <T> value(value: T): Predicate<T> = ValuePredicate(value)

fun <T> Predicate<T>.isTrueThat(test: T.() -> Boolean): Predicate<Boolean> =
    predicateFunction(this) {
        it.test()
    }

fun <T> Predicate<T>.isFalseThat(test: T.() -> Boolean): Predicate<Boolean> =
    isTrueThat(test).not()