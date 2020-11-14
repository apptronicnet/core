package net.apptronic.core.entity.commons

import net.apptronic.core.context.Context
import net.apptronic.core.context.Contextual
import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.base.Property

fun <T> Contextual.nullValue(): Property<T?> =
        ConstantEntity<T?>(context, null)

fun <T> Contextual.ofValue(value: T): Property<T> =
        ConstantEntity(context, value)


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
        context: Context,
        private val value: T
) : BaseProperty<T>(context) {

    init {
        subject.update(value)
    }

}