package net.apptronic.core.component.entity

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.Observer
import net.apptronic.core.component.context.Context

/**
 * Entity is [Observable] which is bound to context
 */
interface Entity<T> : Observable<T> {

    fun getContext(): Context

    override fun subscribe(observer: Observer<T>): EntitySubscription

}

