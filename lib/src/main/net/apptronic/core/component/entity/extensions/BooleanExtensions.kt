package net.apptronic.core.component.entity.extensions

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.functions.entityFunction

fun <E> Entity<Boolean>.selectIf(ifTrue: Entity<E>, ifFalse: Entity<E>): Entity<E> {
    return entityFunction(this, ifTrue, ifFalse) { value, left, right ->
        if (value) left else right
    }
}

fun <E> Entity<Boolean>.selectIf(ifTrue: Entity<E>, ifFalse: E): Entity<E> {
    return entityFunction(this, ifTrue) { value, left ->
        if (value) left else ifFalse
    }
}

fun <E> Entity<Boolean>.selectIf(ifTrue: E, ifFalse: Entity<E>): Entity<E> {
    return entityFunction(this, ifFalse) { value, right ->
        if (value) ifTrue else right
    }
}