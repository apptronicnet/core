package net.apptronic.core.entity.commons

import net.apptronic.core.base.subject.BehaviorSubject
import net.apptronic.core.base.subject.ValueHolder
import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.ObservableSubjectEntity
import net.apptronic.core.entity.base.Property

/**
 * Create new [Entity] with [Unit] constant
 */
fun Contextual.unitEntity(): Entity<Unit> {
    return ConstantEntity(context, Unit)
}

/**
 * Entity with constant value
 */
class ConstantEntity<T>(
        override val context: Context,
        private val value: T
) : ObservableSubjectEntity<T>(), Property<T> {

    override val subject = BehaviorSubject<T>()

    init {
        subject.update(value)
    }

    override fun getValueHolder(): ValueHolder<T>? {
        return subject.getValue()
    }

}