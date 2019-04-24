package net.apptronic.core.component.entity.behavior

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Predicate
import net.apptronic.core.component.entity.PredicateObserver
import net.apptronic.core.component.entity.Subscription
import net.apptronic.core.component.entity.subscribe

class ContextSwitchPredicate<T>(
    private val target: Predicate<T>,
    private val context: Context
) : Predicate<T> {

    override fun subscribe(observer: PredicateObserver<T>): Subscription {
        return target.subscribe(context) { value ->
            observer.notify(value)
        }
    }

}