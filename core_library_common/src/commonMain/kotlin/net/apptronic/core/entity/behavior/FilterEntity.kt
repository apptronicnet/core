package net.apptronic.core.entity.behavior

import kotlinx.coroutines.CoroutineScope
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.context.Context
import net.apptronic.core.context.coroutines.lifecycleCoroutineScope
import net.apptronic.core.context.coroutines.serialThrottler
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.RelayEntity
import net.apptronic.core.entity.function.map

fun <T> Entity<T>.filter(filterFunction: (T) -> Boolean): Entity<T> {
    return FilterEntity(this, filterFunction)
}

fun <T> Entity<T>.filterSuspend(filterFunction: suspend CoroutineScope.(T) -> Boolean): Entity<T> {
    return FilterEntitySuspend(this, filterFunction)
}

inline fun <T, reified E> Entity<T>.filterIs(noinline filterFunction: (E) -> Boolean = { true }): Entity<E> {
    return filter {
        it is E && filterFunction(it)
    }.map { it as E }
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

fun <T> Entity<T>.filterEquals(vararg values: T): Entity<T> {
    return FilterEntity(this) {
        values.contains(it)
    }
}

fun <T> Entity<T>.filterEquals(values: Collection<T>): Entity<T> {
    return FilterEntity(this) {
        values.contains(it)
    }
}

fun <T> Entity<T>.filterNotEquals(vararg values: T): Entity<T> {
    return FilterEntity(this) {
        values.contains(it).not()
    }
}

fun <T> Entity<T>.filterNotEquals(values: Collection<T>): Entity<T> {
    return FilterEntity(this) {
        values.contains(it).not()
    }
}

private class FilterEntity<T>(
        source: Entity<T>,
        private val filterFunction: (T) -> Boolean
) : RelayEntity<T>(source) {

    override fun onNext(nextValue: T, observer: Observer<T>) {
        if (filterFunction(nextValue)) {
            observer.update(nextValue)
        }
    }

}

private class FilterEntitySuspend<T>(
        source: Entity<T>,
        private val filterFunction: suspend CoroutineScope.(T) -> Boolean
) : RelayEntity<T>(source) {

    override fun onNewObserver(targetContext: Context, observer: Observer<T>): Observer<T> {
        val coroutineScope = targetContext.lifecycleCoroutineScope
        return FilterSuspendObserver(observer, coroutineScope, filterFunction)
    }

}

private class FilterSuspendObserver<T>(
        private val target: Observer<T>,
        private val coroutineScope: CoroutineScope,
        private val filterFunction: suspend CoroutineScope.(T) -> Boolean
) : Observer<T> {

    private val coroutineThrottler = coroutineScope.serialThrottler()

    override fun update(value: T) {
        coroutineThrottler.launch {
            if (filterFunction(value)) {
                target.update(value)
            }
        }
    }

}