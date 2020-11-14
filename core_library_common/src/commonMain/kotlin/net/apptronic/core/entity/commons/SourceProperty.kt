package net.apptronic.core.entity.commons

import net.apptronic.core.context.Context
import net.apptronic.core.entity.Entity

class SourceProperty<T>(context: Context, source: Entity<T>) : SimpleProperty<T>(context) {

    init {
        source.subscribe(context, subject)
    }

}