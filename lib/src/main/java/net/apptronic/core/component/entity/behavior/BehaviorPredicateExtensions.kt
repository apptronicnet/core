package net.apptronic.core.component.entity.behavior

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.functions.variants.map
import net.apptronic.core.threading.Worker
import net.apptronic.core.threading.WorkerDefinition

fun <T> Predicate<T>.switchWorker(worker: Worker): Predicate<T> {
    return WorkerSwitchPredicate(this, worker)
}

fun <T> Predicate<T>.switchWorker(
    context: Context,
    workerDefinition: WorkerDefinition
): Predicate<T> {
    val worker = context.getScheduler().getWorker(workerDefinition)
    return WorkerSwitchPredicate(this, worker)
}

fun <T> Predicate<T>.switchContext(context: Context): Predicate<T> {
    return ContextSwitchPredicate(
        this, context
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
