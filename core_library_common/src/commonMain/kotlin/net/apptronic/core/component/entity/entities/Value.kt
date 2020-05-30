package net.apptronic.core.component.entity.entities

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.distinctUntilChanged
import net.apptronic.core.component.context.Context

class Value<T>(context: Context) : AbstractValue<T>(context) {

    override val observable: Observable<T> = subject.distinctUntilChanged()

}

