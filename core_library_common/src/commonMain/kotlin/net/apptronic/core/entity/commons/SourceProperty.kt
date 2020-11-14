package net.apptronic.core.entity.commons

import net.apptronic.core.base.utils.EqComparator
import net.apptronic.core.base.utils.SimpleEqComparator
import net.apptronic.core.context.Context
import net.apptronic.core.entity.Entity

class SourceProperty<T>(
        context: Context, source: Entity<T>, eqComparator: EqComparator<T> = SimpleEqComparator<T>()
) : BaseProperty<T>(context, eqComparator) {

    init {
        source.subscribe(context, subject)
    }

}