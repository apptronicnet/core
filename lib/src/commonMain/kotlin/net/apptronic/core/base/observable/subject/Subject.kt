package net.apptronic.core.base.observable.subject

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.Observer

interface Subject<T> : Observable<T>, Observer<T> {

    fun update(value: T)

    override fun notify(value: T) {
        update(value)
    }

}