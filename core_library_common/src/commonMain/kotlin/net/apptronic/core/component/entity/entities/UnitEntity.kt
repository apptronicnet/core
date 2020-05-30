package net.apptronic.core.component.entity.entities

import net.apptronic.core.base.observable.subject.BehaviorSubject
import net.apptronic.core.component.context.Context
import net.apptronic.core.component.entity.base.SubjectEntity
import net.apptronic.core.component.entity.Entity
/**
 * Simple [Entity] which emits [Unit] on subscribe
 */
class UnitEntity(
        override val context: Context
) : SubjectEntity<Unit>() {

    override val subject = BehaviorSubject<Unit>().apply { update(Unit) }

}