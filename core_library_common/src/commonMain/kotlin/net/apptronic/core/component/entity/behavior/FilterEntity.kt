package net.apptronic.core.component.entity.behavior

import kotlinx.coroutines.CoroutineScope
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.coroutines.coroutineLauncherScoped
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.functions.map
import net.apptronic.core.component.entity.subscribe

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
        private val target: Entity<T>,
        private val filterFunction: (T) -> Boolean
) : Entity<T> {

    override val context: Context = target.context

    override fun subscribe(observer: Observer<T>): EntitySubscription {
        return target.subscribe { value ->
            if (filterFunction(value)) {
                observer.notify(value)
            }
        }
    }

    override fun subscribe(context: Context, observer: Observer<T>): EntitySubscription {
        return target.subscribe(context) { value ->
            if (filterFunction(value)) {
                observer.notify(value)
            }
        }
    }

}

private class FilterEntitySuspend<T>(
        private val target: Entity<T>,
        private val filterFunction: suspend CoroutineScope.(T) -> Boolean
) : Entity<T> {

    override val context: Context = target.context

    private val coroutineLauncher = context.coroutineLauncherScoped()

    override fun subscribe(observer: Observer<T>): EntitySubscription {
        return target.subscribe { value ->
            coroutineLauncher.launch {
                if (filterFunction(value)) {
                    observer.notify(value)
                }
            }
        }
    }

    override fun subscribe(context: Context, observer: Observer<T>): EntitySubscription {
        return target.subscribe(context) { value ->
            coroutineLauncher.launch {
                if (filterFunction(value)) {
                    observer.notify(value)
                }
            }
        }
    }

}