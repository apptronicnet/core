package net.apptronic.common.core.component.entity.behavior

import net.apptronic.common.core.component.ComponentContext
import net.apptronic.common.core.component.entity.Predicate

fun <T> Predicate<T>.switchWorker(context: ComponentContext, workerName: String): Predicate<T> {
    return WorkerSwitchPredicate(
        this, context.workers(), workerName
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
