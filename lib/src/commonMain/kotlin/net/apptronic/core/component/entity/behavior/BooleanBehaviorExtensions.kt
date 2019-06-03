package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.subscribe
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.functions.entityFunction
import net.apptronic.core.component.entity.functions.variants.map


fun Entity<Boolean>.whenTrue(): Entity<Unit> {
    return filter { it }.map { Unit }
}

fun Entity<Boolean>.whenFalse(): Entity<Unit> {
    return filter { it.not() }.map { Unit }
}

fun Entity<Boolean?>.whenTrueNotNull(): Entity<Unit> {
    return filter { it == true }.map { Unit }
}

fun Entity<Boolean?>.whenFalseNotNull(): Entity<Unit> {
    return filter { it == false }.map { Unit }
}

fun Entity<Boolean?>.whenTrueOrNull(): Entity<Boolean?> {
    return filter { it != false }
}

fun Entity<Boolean?>.whenFalseOrNull(): Entity<Boolean?> {
    return filter { it != true }
}

fun Entity<Boolean>.doWhenTrue(action: () -> Unit): Entity<Boolean> {
    whenTrue().subscribe { action.invoke() }
    return this
}

fun Entity<Boolean>.doWhenFalse(action: () -> Unit): Entity<Boolean> {
    whenFalse().subscribe { action.invoke() }
    return this
}

fun whenever(entity: Entity<Boolean>, action: () -> Unit) {
    entity.doWhenTrue(action)
}

fun wheneverNot(entity: Entity<Boolean>, action: () -> Unit) {
    entity.doWhenFalse(action)
}

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