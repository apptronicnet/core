package net.apptronic.core.entity.function

import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.Property
import net.apptronic.core.entity.behavior.filter

class LeftRight<A, B>(
    val left: Entity<A>,
    val right: Entity<B>
)

class Whenever(
    val condition: Entity<Boolean>
)

infix fun <A, B> Entity<A>.orElse(another: Entity<B>): LeftRight<A, B> =
    LeftRight(this, another)

fun whenever(condition: Entity<Boolean>): Whenever {
    return Whenever(condition)
}

infix fun <T> Whenever.then(leftRight: LeftRight<T, T>) =
    entityFunction(this.condition, pair(leftRight.left, leftRight.right)) { condition, pair ->
        if (condition) pair.first else pair.second
    }

infix fun <T> Whenever.then(entity: Entity<T>): Property<T> {
    return pair(this.condition, entity)
            .filter {
                it.first
            }.map {
                it.second
            }
}

