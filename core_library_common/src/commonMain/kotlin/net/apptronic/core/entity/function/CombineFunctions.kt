package net.apptronic.core.entity.function

import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.Property

fun <A, B> pair(left: Entity<A>, right: Entity<B>): Property<Pair<A, B>> =
        entityFunction(left, right) { a, b ->
            a to b
        }