package net.apptronic.core.component.entity.extensions

import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.functions.predicateFunction

fun <E> Predicate<Boolean>.selectIf(ifTrue: Predicate<E>, ifFalse: Predicate<E>): Predicate<E> {
    return predicateFunction(this, ifTrue, ifFalse) { value, left, right ->
        if (value) left else right
    }
}

fun <E> Predicate<Boolean>.selectIf(ifTrue: Predicate<E>, ifFalse: E): Predicate<E> {
    return predicateFunction(this, ifTrue) { value, left ->
        if (value) left else ifFalse
    }
}

fun <E> Predicate<Boolean>.selectIf(ifTrue: E, ifFalse: Predicate<E>): Predicate<E> {
    return predicateFunction(this, ifFalse) { value, right ->
        if (value) ifTrue else right
    }
}