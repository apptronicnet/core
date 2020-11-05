package net.apptronic.core.entity.function

import net.apptronic.core.entity.Entity

fun <T> Entity<T?>.asString(): Entity<String> =
        entityFunction(this) {
            it.toString()
        }

operator fun <A, B> Entity<A>.plus(another: Entity<B>): Entity<String> =
        entityFunction(this, another) { left, right ->
            left.toString() + right.toString()
        }


fun concat(concatenation: String, vararg entities: Entity<*>): Entity<String> =
        entityArrayFunction(arrayOf(*entities)) {
            it.joinToString(separator = concatenation)
        }
