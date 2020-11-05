package net.apptronic.core.context.di

import kotlin.reflect.KClass

/**
 * Unique key for object in DI context
 */
data class ObjectKey internal constructor(
        val clazz: KClass<*>?,
        val descriptor: DependencyDescriptor<*>?
) : Comparable<ObjectKey> {

    override fun toString(): String {
        return descriptor?.toString() ?: "Class=$clazz"
    }

    override fun compareTo(other: ObjectKey): Int {
        return toString().compareTo(other.toString())
    }

}

internal fun objectKey(clazz: KClass<*>): ObjectKey {
    return ObjectKey(clazz, null)
}

internal fun objectKey(descriptor: DependencyDescriptor<*>): ObjectKey {
    return ObjectKey(null, descriptor)
}