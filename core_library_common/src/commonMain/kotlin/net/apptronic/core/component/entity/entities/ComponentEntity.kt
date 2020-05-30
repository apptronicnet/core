package net.apptronic.core.component.entity.entities

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.base.ObservableEntity

abstract class ComponentEntity<T>(
        override val context: Context
) : ObservableEntity<T>() {

    abstract override val observable: Observable<T>

}