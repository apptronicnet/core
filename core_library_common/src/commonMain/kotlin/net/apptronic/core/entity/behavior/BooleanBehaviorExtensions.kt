package net.apptronic.core.entity.behavior

import kotlinx.coroutines.CoroutineScope
import net.apptronic.core.base.subject.asValueHolder
import net.apptronic.core.base.subject.unwrapValueHolder
import net.apptronic.core.base.subject.wrapValueHolder
import net.apptronic.core.context.Contextual
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.Property
import net.apptronic.core.entity.commons.BaseMutableValue
import net.apptronic.core.entity.commons.setAs
import net.apptronic.core.entity.commons.withDefaultNull
import net.apptronic.core.entity.function.anyValue
import net.apptronic.core.entity.function.entityFunction
import net.apptronic.core.entity.function.map

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

fun Contextual.whenever(entity: Entity<Boolean>, action: () -> Unit) {
    entity.switchContext(context).doWhenTrue(action)
}

fun Contextual.wheneverNot(entity: Entity<Boolean>, action: () -> Unit) {
    entity.switchContext(context).doWhenFalse(action)
}

fun <E> Entity<Boolean>.selectIf(ifTrue: Entity<E>): Entity<E> {
    return entityFunction(this, ifTrue) { value, ifTrueValue ->
        if (value) ifTrueValue.asValueHolder() else null
    }.filterNotNull().unwrapValueHolder()
}

fun <E> Entity<Boolean>.selectIf(ifTrue: Entity<E>, ifFalse: Entity<E>): Entity<E> {
    return entityFunction(
            this,
            ifTrue.wrapValueHolder().withDefaultNull(),
            ifFalse.wrapValueHolder().withDefaultNull()
    ) { value, left, right ->
        if (value) left else right
    }.filterNotNull().unwrapValueHolder()
}

fun <E> Entity<Boolean>.selectIf(ifTrue: Entity<E>, ifFalse: E): Entity<E> {
    return entityFunction(this, ifTrue.wrapValueHolder().withDefaultNull()) { value, left ->
        if (value) left else ifFalse.asValueHolder()
    }.filterNotNull().unwrapValueHolder()
}

fun <E> Entity<Boolean>.selectIf(ifTrue: E, ifFalse: Entity<E>): Entity<E> {
    return entityFunction(this, ifFalse.wrapValueHolder().withDefaultNull()) { value, right ->
        if (value) ifTrue.asValueHolder() else right
    }.filterNotNull().unwrapValueHolder()
}

/**
 * Emits new [Entity] which emit false until no value set is source [Entity]
 * and emits true when source [Entity] emitted any value.
 */
fun <T> Entity<T>.whenAnyValue(): Property<Boolean> {
    return BaseMutableValue<Boolean>(context).also {
        it.set(false)
        it.setAs(anyValue())
    }
}


/**
 * Emits new [Entity] which emit false until no value set is source [Entity]
 * and emits true when [filterFunction] returned true for [Entity] value.
 */
fun <T> Entity<T>.whenAny(filterFunction: (T) -> Boolean): Property<Boolean> {
    return BaseMutableValue<Boolean>(context).also {
        it.set(false)
        it.setAs(filter(filterFunction).anyValue())
    }
}

fun <T> Entity<T>.whenAnySuspend(filterFunction: suspend CoroutineScope.(T) -> Boolean): Property<Boolean> {
    return BaseMutableValue<Boolean>(context).also {
        it.set(false)
        it.setAs(filterSuspend(filterFunction).anyValue())
    }
}