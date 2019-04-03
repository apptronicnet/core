package net.apptronic.core.component.entity.behavior

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Predicate

fun <T> Predicate<T>.switchWorker(context: Context, workerName: String): Predicate<T> {
    return WorkerSwitchPredicate(
        this, context.getWorkers(), workerName
    )
}

fun <T> Predicate<T>.filter(filterFunction: (T) -> Boolean): Predicate<T> {
    return FilterPredicate(this, filterFunction)
}

fun <T> Predicate<T>.filterNot(filterNotFunction: (T) -> Boolean): Predicate<T> {
    return FilterPredicate(this) {
        filterNotFunction(it).not()
    }
}
