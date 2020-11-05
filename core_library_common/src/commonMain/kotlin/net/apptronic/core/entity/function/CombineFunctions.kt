package net.apptronic.core.entity.function

import net.apptronic.core.entity.Entity

fun <A, B> pair(left: Entity<A>, right: Entity<B>): Entity<Pair<A, B>> =
    entityFunction(left, right) { a, b ->
        a to b
    }