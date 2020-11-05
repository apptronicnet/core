package net.apptronic.core.entity.entities

import net.apptronic.core.base.subject.BehaviorSubject
import net.apptronic.core.context.Context
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.SubjectEntity

/**
 * Simple [Entity] which emits [Unit] on subscribe
 */
class UnitEntity(
        override val context: Context
) : SubjectEntity<Unit>() {

    override val subject = BehaviorSubject<Unit>().apply { update(Unit) }

}