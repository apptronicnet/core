package net.apptronic.core.component.entity.functions

import net.apptronic.core.component.entity.Entity

fun <A, B> pair(left: Entity<A>, right: Entity<B>): Entity<Pair<A, B>> =
    entityFunction(left, right) { a, b ->
        a to b
    }