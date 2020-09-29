package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.subject.PublishSubject
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.base.SubjectEntity
import net.apptronic.core.component.entity.entities.asEvent
import net.apptronic.core.component.entity.switchContext

/**
 * This creates new Entity which emits Unit signal each time when any of [sources] emits any item. Ignores values
 * of currently set items in properties etc.
 */
fun Context.combineAsSignals(vararg sources: Entity<*>): Entity<Unit> {
    return CombineSignalEntity(this, sources)
}

private class CombineSignalEntity(
        override val context: Context,
        private val sources: Array<out Entity<*>>
) : SubjectEntity<Unit>() {

    override val subject = PublishSubject<Unit>()

    init {
        var started = false
        // this is for ignoring any currently set items
        sources.forEach {
            it.switchContext(context).asEvent().subscribe {
                if (started) {
                    subject.update(Unit)
                }
            }
        }
        started = true
    }

}