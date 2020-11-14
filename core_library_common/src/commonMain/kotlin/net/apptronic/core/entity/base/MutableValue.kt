package net.apptronic.core.entity.base

import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.function.map

/**
 * Type of [Value] which can distinct between set and update actions.
 */
interface MutableValue<T> : Value<T> {

    data class Change<T>(
            /**
             * Next value emitted by this entity
             */
            val value: T,
            /**
             * True when it updated externally by call [isUpdate].
             * False for initial state set up, internal calculations or other changes, not related to [isUpdate] call.
             */
            val isUpdate: Boolean
    ) {

        fun <E> map(function: (T) -> E): Change<E> {
            return Change(function(value), isUpdate)
        }

    }

    /**
     * Get [Entity] which emits [Change] instead of [T]
     */
    val changes: Entity<Change<T>>

    /**
     * Get [Entity] which emits only changes made by [update] call
     */
    val updates: Entity<T>

    /**
     * Send entity update. Will emit new item and send [Change] with [Change.isUpdate] = true
     */
    override fun update(value: T)

    /**
     * Set [value] and notify all observers of it. Does not send notification to [updates],
     * send notification to [changes] with [Notification::isUpdate] = false
     */
    override fun set(value: T)

    /**
     * Apply [change] to this [MutableValue]
     */
    fun applyChange(change: Change<T>)

    /**
     * Apply [Change] to this [MutableValue] with composing [value] and [isUpdate]
     */
    fun applyChange(value: T, isUpdate: Boolean)

}

fun <T, E> Entity<MutableValue.Change<T>>.mapChange(
        function: (T) -> E
): Entity<MutableValue.Change<E>> {
    return map { it.map(function) }
}