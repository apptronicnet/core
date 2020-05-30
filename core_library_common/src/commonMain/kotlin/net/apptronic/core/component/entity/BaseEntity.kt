package net.apptronic.core.component.entity

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.lifecycle.Lifecycle

/**
 * Entity is [Observable] which is bound to [Context] and automatically works with it's [Lifecycle].
 */
abstract class BaseEntity<T> : Entity<T> {

    final override fun subscribe(observer: Observer<T>): EntitySubscription {
        return subscribe(context, observer)
    }

}

