package net.apptronic.core.entity.commons

import net.apptronic.core.entity.Entity
import net.apptronic.core.entity.ValueNotSetException
import net.apptronic.core.entity.base.EntityValue
import net.apptronic.core.entity.base.UpdateEntity
import net.apptronic.core.entity.behavior.filter
import net.apptronic.core.entity.function.map

/**
 * Type of [EntityValue] which can be mutated by setting new value
 */
interface MutableEntity<T> : EntityValue<T>, UpdateEntity<T> {

    data class Notification<T>(
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

        fun <E> map(function: (T) -> E): Notification<E> {
            return Notification(function(value), isUpdate)
        }

    }

    /**
     * Get [Entity] which emits [Notification] instead of [T]
     */
    val notifications: Entity<Notification<T>>

    /**
     * Send entity update. Will emit new item and send [Notification] with [Notification.isUpdate] = true
     */
    override fun update(value: T)

    /**
     * Set [value] and notify all observers of it. Does not send notification to [updates],
     * send notification to [notifications] with [Notification::isUpdate] = false
     */
    fun set(value: T)

}

/**
 * Set current value from given [source]
 * @return true if value set, false if no value set inside [source]
 */
fun <T> MutableEntity<T>.setFrom(source: EntityValue<T>): Boolean {
    return try {
        set(source.get())
        true
    } catch (e: ValueNotSetException) {
        false
    }
}

val <T> MutableEntity<T>.updates: Entity<T>
    get() = notifications.filter { it.isUpdate }.map { it.value }

fun <T> MutableEntity<T>.mutateUsingNotification(notification: MutableEntity.Notification<T>) {
    if (notification.isUpdate) {
        update(notification.value)
    } else {
        set(notification.value)
    }
}

fun <T> MutableEntity<T>.mutateUsingNotification(value: T, isUpdate: Boolean) {
    if (isUpdate) {
        update(value)
    } else {
        set(value)
    }
}

fun <T, E> Entity<MutableEntity.Notification<T>>.mapNotification(
        function: (T) -> E
): Entity<MutableEntity.Notification<E>> {
    return map { it.map(function) }
}