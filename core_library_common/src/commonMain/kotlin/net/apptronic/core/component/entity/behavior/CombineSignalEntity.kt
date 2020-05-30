package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subject.PublishSubject
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.base.SubjectEntity
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.component.entity.subscriptions.ContextSubjectWrapper

/**
 * This creates new Entity which emits Unit signal each time when any of [sources] emits any item.
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
        sources.forEach {
            it.subscribe(context) {
                subject.update(Unit)
            }
        }
    }

}