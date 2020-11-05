package net.apptronic.core.entity.behavior

import net.apptronic.core.base.subject.PublishSubject
import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.SubjectEntity
import net.apptronic.core.entity.commons.asEvent

/**
 * This creates new Entity which emits Unit signal each time when any of [sources] emits any item. Ignores values
 * of currently set items in properties etc.
 */
fun Contextual.whenAnyUpdates(vararg sources: Entity<*>): Entity<Unit> {
    return WhenAnyUpdatesEntity(context, sources)
}

private class WhenAnyUpdatesEntity(
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