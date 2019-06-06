package net.apptronic.core.component.entity.entities

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.distinctUntilChanged
import net.apptronic.core.component.context.Context

class Value<T>(context: Context) : Property<T>(context) {

    private val observable = subject.distinctUntilChanged()

    override fun getObservable(): Observable<T> {
        return observable
    }
}

