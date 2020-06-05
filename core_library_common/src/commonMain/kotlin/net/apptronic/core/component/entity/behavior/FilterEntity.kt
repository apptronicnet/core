package net.apptronic.core.component.entity.behavior

import kotlinx.coroutines.CoroutineScope
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.coroutines.CoroutineLauncher
import net.apptronic.core.component.coroutines.coroutineLauncherScoped
import net.apptronic.core.component.coroutines.serial
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.base.RelayEntity
import net.apptronic.core.component.entity.functions.map

fun <T> Entity<T>.filter(filterFunction: (T) -> Boolean): Entity<T> {
    return FilterEntity(this, filterFunction)
}

fun <T> Entity<T>.filterSuspend(filterFunction: suspend CoroutineScope.(T) -> Boolean): Entity<T> {
    return FilterEntitySuspend(this, filterFunction)
}

fun <T> Entity<T?>.filterNotNull(): Entity<T> {
    return filter { it != null }.map { it!! }
}

fun <T> Entity<T>.filterNot(filterNotFunction: (T) -> Boolean): Entity<T> {
    return FilterEntity(this) {
        filterNotFunction(it).not()
    }
}

fun <T> Entity<T>.filterNotSuspend(filterNotFunction: (T) -> Boolean): Entity<T> {
    return FilterEntitySuspend(this) {
        filterNotFunction(it).not()
    }
}

private class FilterEntity<T>(
        source: Entity<T>,
        private val filterFunction: (T) -> Boolean
) : RelayEntity<T>(source) {

    override fun onNext(nextValue: T, observer: Observer<T>) {
        if (filterFunction(nextValue)) {
            observer.notify(nextValue)
        }
    }

}

private class FilterEntitySuspend<T>(
        source: Entity<T>,
        private val filterFunction: suspend CoroutineScope.(T) -> Boolean
) : RelayEntity<T>(source) {

    override fun proceedObserver(targetContext: Context, target: Observer<T>): Observer<T> {
        val coroutineLauncher = targetContext.coroutineLauncherScoped().serial()
        return FilterSuspendObserver(target, coroutineLauncher, filterFunction)
    }

}

private class FilterSuspendObserver<T>(
        private val target: Observer<T>,
        private val coroutineLauncher: CoroutineLauncher,
        private val filterFunction: suspend CoroutineScope.(T) -> Boolean
) : Observer<T> {

    override fun notify(value: T) {
        coroutineLauncher.launch {
            if (filterFunction(value)) {
                target.notify(value)
            }
        }
    }

}