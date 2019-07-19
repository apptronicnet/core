package net.apptronic.core.component.entity.behavior

import net.apptronic.core.base.observable.Observer
import net.apptronic.core.base.observable.subject.PublishSubject
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.Entity
import net.apptronic.core.component.entity.EntitySubscription
import net.apptronic.core.component.entity.subscribe
import net.apptronic.core.component.entity.subscriptions.ContextSubjectWrapper

/**
 * This creates new Entity which emits Unit signal each time when any of [sources] emits any item.
 */
fun Context.combineAsSignals(vararg sources: Entity<*>): Entity<Unit> {
    return CombineSignalEntity(this, sources)
}

private class CombineSignalEntity(
        private val context: Context,
        private val sources: Array<out Entity<*>>
) : Entity<Unit> {

    private val subject = ContextSubjectWrapper(context, PublishSubject<Unit>())

    init {
        sources.forEach {
            it.subscribe(context) {
                subject.update(Unit)
            }
        }
    }

    override fun getContext(): Context {
        return context
    }

    override fun subscribe(context: Context, observer: Observer<Unit>): EntitySubscription {
        return subject.subscribe(context, observer)
    }

}