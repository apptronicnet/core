package net.apptronic.core.component.entity.entities

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.base.observable.subject.PublishSubject
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.base.SubjectEntity

/**
 * Simple [Entity] which never emits anything
 */
class EmptyEntity(
        override val context: Context
) : SubjectEntity<Unit>() {

    override val subject = PublishSubject<Unit>()

}