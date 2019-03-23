package net.apptronic.common.core.component.entity.base

import net.apptronic.common.core.component.entity.Subscription

/**
 * Predicate which is store values
 */
open class UpdateAndStorePredicate<T> : UpdatePredicate<T>() {

    @Volatile
    private var valueHolder: ValueHolder<T>? = null

    override fun update(value: T) {
        valueHolder = ValueHolder(value)
        super.update(value)
    }

    override fun subscribe(observer: (T) -> Unit): Subscription {
        valueHolder?.let {
            observer.invoke(it.value)
        }
        return super.subscribe(observer)
    }

}