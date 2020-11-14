package net.apptronic.core.base.subject

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.Observer

interface Subject<T> : Observable<T>, Observer<T> {

    override fun update(value: T)

}