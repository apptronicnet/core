package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.functions.variants.map
import net.apptronic.core.component.entity.subscribe

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

private class FilterEntity<T>(
    private val target: Entity<T>,
    private val filterFunction: (T) -> Boolean
) : Entity<T> {

    override fun getContext(): Context {
        return target.getContext()
    }

    override fun subscribe(observer: Observer<T>): EntitySubscription {
        return target.subscribe { value ->
            if (filterFunction(value)) {
                observer.notify(value)
            }
        }
    }

}