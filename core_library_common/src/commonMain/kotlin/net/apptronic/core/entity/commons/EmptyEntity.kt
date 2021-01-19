package net.apptronic.core.entity.commons

import net.apptronic.core.base.subject.PublishSubject
import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.ObservableSubjectEntity

fun <T> Contextual.emptyEntity(): Entity<T> {
    return EmptyEntity(context)
}

/**
 * Simple [Entity] which never emits anything.
 */
class EmptyEntity<T>(
    override val context: Context
) : ObservableSubjectEntity<T>() {

    override val subject = PublishSubject<T>()

}