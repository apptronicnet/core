package net.apptronic.core.component.entity.behavior

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.functions.variants.map
import net.apptronic.core.threading.Worker
import net.apptronic.core.threading.WorkerDefinition

fun <T> Entity<T>.switchWorker(worker: Worker): Entity<T> {
    return WorkerSwitchEntity(this, worker)
}

fun <T> Entity<T>.switchWorker(
    context: Context,
    workerDefinition: WorkerDefinition
): Entity<T> {
    val worker = context.getScheduler().getWorker(workerDefinition)
    return WorkerSwitchEntity(this, worker)
}

fun <T> Entity<T>.switchContext(context: Context): Entity<T> {
    return ContextSwitchEntity(
        this, context
    )
}

fun <T> Entity<T>.filter(filterFunction: (T) -> Boolean): Entity<T> {
    return FilterEntity(this, filterFunction)
}

fun <T> Entity<T?>.filterNotNull(): Entity<T> {
    return filter { it != null }.map { it!! }
}

fun <T> Entity<T>.filterNot(filterNotFunction: (T) -> Boolean): Entity<T> {
    return FilterEntity(this) {
        filterNotFunction(it).not()
    }
}
