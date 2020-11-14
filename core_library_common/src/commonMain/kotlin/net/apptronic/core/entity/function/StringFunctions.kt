package net.apptronic.core.entity.function

import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.Property

fun <T> Entity<T?>.asString(): Property<String> =
        entityFunction(this) {
            it.toString()
        }

operator fun <A, B> Entity<A>.plus(another: Entity<B>): Property<String> =
        entityFunction(this, another) { left, right ->
            left.toString() + right.toString()
        }


fun concat(concatenation: String, vararg entities: Entity<*>): Property<String> =
        entityArrayFunction(arrayOf(*entities)) {
            it.joinToString(separator = concatenation)
        }
