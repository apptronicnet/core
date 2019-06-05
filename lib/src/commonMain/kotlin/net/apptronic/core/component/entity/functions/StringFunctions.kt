package net.apptronic.core.component.entity.functions

import net.apptronic.core.component.entity.Entity

fun <T> Entity<T?>.asString(): Entity<String> =
    entityFunction(this) {
        it.toString()
    }

infix fun <A, B> Entity<A>.concat(another: Entity<B>): Entity<String> =
    entityFunction(this, another) { left, right ->
        left.toString() + right.toString()
    }


fun concat(concatenation: String, vararg entities: Entity<*>): Entity<String> =
    entityArrayFunction(arrayOf(*entities)) {
        it.joinToString(separator = concatenation)
    }
