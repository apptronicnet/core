package net.apptronic.common.android.ui.viewmodel.entity.functions.variants

import net.apptronic.common.android.ui.viewmodel.entity.functions.Predicate
import net.apptronic.common.android.ui.viewmodel.entity.functions.predicateArrayFunction
import net.apptronic.common.android.ui.viewmodel.entity.functions.predicateFunction

fun <T> Predicate<T?>.asString(): Predicate<String> =
    predicateFunction(this) {
        it.toString()
    }

infix fun <A, B> Predicate<A>.concat(another: Predicate<B>): Predicate<String> =
    predicateFunction(this, another) { left, right ->
        left.toString() + right.toString()
    }


fun concat(concatenation: String, vararg predicates: Predicate<*>): Predicate<String> =
    predicateArrayFunction(arrayOf(*predicates)) {
        it.joinToString(separator = concatenation)
    }
