package net.apptronic.core.component.entity.functions.variants

import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.behavior.filter
import net.apptronic.core.component.entity.functions.predicateFunction

class LeftRight<A, B>(
    val left: Predicate<A>,
    val right: Predicate<B>
)

class Whenever(
    val condition: Predicate<Boolean>
)

infix fun <A, B> Predicate<A>.orElse(another: Predicate<B>): LeftRight<A, B> =
    LeftRight(this, another)

fun whenever(condition: Predicate<Boolean>): Whenever {
    return Whenever(condition)
}

infix fun <T> Whenever.then(leftRight: LeftRight<T, T>): Predicate<T> =
    predicateFunction(this.condition, pair(leftRight.left, leftRight.right)) { condition, pair ->
        if (condition) pair.first else pair.second
    }

infix fun <T> Whenever.then(predicate: Predicate<T>): Predicate<T> {
    return pair(this.condition, predicate)
        .filter {
            it.first
        }.map {
            it.second
        }
}

