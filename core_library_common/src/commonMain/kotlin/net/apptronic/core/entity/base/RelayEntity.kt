package net.apptronic.core.entity.base

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.context.Context
import net.apptronic.core.entity.BaseEntity
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.EntitySubscription

/**
 * [Entity] which based on relaying [source] [Entity]
 */
abstract class RelayEntity<T>(val source: Entity<T>) : BaseEntity<T>() {

    final override val context: Context = source.context

    final override fun onSubscribeObserver(targetContext: Context, targetObserver: Observer<T>): EntitySubscription {
        return source.subscribe(targetContext) { value ->
            onNext(value, targetObserver)
        }
    }

    /**
     * Notify [observer] by [nextValue] if needed
     */
    open fun onNext(nextValue: T, observer: Observer<T>) {
        observer.update(nextValue)
    }

}