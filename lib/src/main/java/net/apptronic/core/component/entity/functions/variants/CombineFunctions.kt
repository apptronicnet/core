package net.apptronic.core.component.entity.functions.variants

import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.functions.predicateFunction

fun <A, B> pair(left: Predicate<A>, right: Predicate<B>): Predicate<Pair<A, B>> =
    predicateFunction(left, right) { a, b ->
        a to b
    }