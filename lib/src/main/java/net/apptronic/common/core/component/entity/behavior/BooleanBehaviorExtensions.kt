package net.apptronic.common.core.component.entity.behavior

import net.apptronic.common.core.component.entity.Predicate
import net.apptronic.common.core.component.entity.functions.variants.map


fun Predicate<Boolean>.whenTrue(): Predicate<Unit> {
    return filter { it }.map { Unit }
}

fun Predicate<Boolean>.whenFalse(): Predicate<Unit> {
    return filter { it.not() }.map { Unit }
}

fun Predicate<Boolean?>.whenTrueNotNull(): Predicate<Unit> {
    return filter { it == true }.map { Unit }
}

fun Predicate<Boolean?>.whenFalseNotNull(): Predicate<Unit> {
    return filter { it == false }.map { Unit }
}

fun Predicate<Boolean?>.whenTrueOrNull(): Predicate<Boolean?> {
    return filter { it != false }
}

fun Predicate<Boolean?>.whenFalseOrNull(): Predicate<Boolean?> {
    return filter { it != true }
}

fun Predicate<Boolean>.doWhenTrue(action: () -> Unit): Predicate<Boolean> {
    whenTrue().subscribe { action.invoke() }
    return this
}

fun Predicate<Boolean>.doWhenFalse(action: () -> Unit): Predicate<Boolean> {
    whenFalse().subscribe { action.invoke() }
    return this
}

fun whenever(predicate: Predicate<Boolean>, action: () -> Unit) {
    predicate.doWhenTrue(action)
}

fun wheneverNot(predicate: Predicate<Boolean>, action: () -> Unit) {
    predicate.doWhenFalse(action)
}