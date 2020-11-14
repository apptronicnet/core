package net.apptronic.core.entity.commons

import net.apptronic.core.context.Context
import net.apptronic.core.entity.Entity

class SourceProperty<T>(context: Context, source: Entity<T>) : BaseProperty<T>(context) {

    init {
        source.subscribe(context, subject)
    }

}