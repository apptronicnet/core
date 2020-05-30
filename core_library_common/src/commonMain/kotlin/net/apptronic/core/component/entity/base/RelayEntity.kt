package net.apptronic.core.component.entity.base

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.BaseEntity
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.subscribe

/**
 * [Entity] which based on relaying [source] [Entity]
 */
abstract class RelayEntity<T>(val source: Entity<T>) : BaseEntity<T>() {

    final override val context: Context = source.context

    final override fun subscribe(targetContext: Context, observer: Observer<T>): EntitySubscription {
        val targetObserver = proceedObserver(targetContext, observer)
        return source.subscribe(targetContext) { value ->
            onNext(value, targetObserver)
        }
    }

    /**
     * Provide real observer for [target]
     */
    open fun proceedObserver(targetContext: Context, target: Observer<T>) : Observer<T> {
        return target
    }

    /**
     * Notify [observer] by [nextValue] if needed
     */
    open fun onNext(nextValue: T, observer: Observer<T>) {
        observer.notify(nextValue)
    }

}