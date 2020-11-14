package net.apptronic.core.base.subject

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.Observer

/**
 * Represents [Observer] and [Observable] at same time, allowing to subscribe and pubish updates
 */
interface Subject<T> : Observable<T>, Observer<T> {

    /**
     * Publish an update to current [Subject]
     */
    override fun update(value: T)

}