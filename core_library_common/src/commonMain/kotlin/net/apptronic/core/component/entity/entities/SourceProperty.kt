package net.apptronic.core.component.entity.entities

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity

class SourceProperty<T>(context: Context, source: Entity<T>) : Property<T>(context) {

    init {
        source.subscribe(context, subject)
    }

}