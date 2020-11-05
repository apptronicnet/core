package net.apptronic.core.entity.commons

import net.apptronic.core.base.subject.PublishSubject
import net.apptronic.core.context.Context
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.SubjectEntity

/**
 * Simple [Entity] which never emits anything
 */
class EmptyEntity(
        override val context: Context
) : SubjectEntity<Unit>() {

    override val subject = PublishSubject<Unit>()

}