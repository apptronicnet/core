package net.apptronic.core.component.entity.entities

import net.apptronic.core.base.observable.Observable
import net.apptronic.core.base.observable.distinctUntilChanged
import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity

class SourceProperty<T>(context: Context, source: Entity<T>) : Property<T>(context) {

    override val observable = subject.distinctUntilChanged()

    init {
        source.subscribe(context, subject)
    }

}