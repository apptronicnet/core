package net.apptronic.core.component.entity.behavior

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.functions.variants.map

fun <T> Predicate<T>.switchWorker(context: Context, workerName: String): Predicate<T> {
    return WorkerSwitchPredicate(
        this, context.getWorkers(), workerName
    )
}

fun <T> Predicate<T>.filter(filterFunction: (T) -> Boolean): Predicate<T> {
    return FilterPredicate(this, filterFunction)
}

fun <T> Predicate<T?>.filterNotNull(): Predicate<T> {
    return filter { it != null }.map { it!! }
}

fun <T> Predicate<T>.filterNot(filterNotFunction: (T) -> Boolean): Predicate<T> {
    return FilterPredicate(this) {
        filterNotFunction(it).not()
    }
}
