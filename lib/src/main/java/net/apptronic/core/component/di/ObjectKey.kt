package net.apptronic.core.component.di

import kotlin.reflect.KClass

/**
 * Unique key for object in DI context
 */
data class ObjectKey internal constructor(
    val className: String,
    val descriptor: Descriptor<*>?
)

fun objectKey(
    className: String,
    descriptor: Descriptor<*>?
): ObjectKey {
    return ObjectKey(className, descriptor)
}

fun objectKey(
    clazz: KClass<*>,
    descriptor: Descriptor<*>?
): ObjectKey {
    val className = clazz.qualifiedName
        ?: throw IllegalArgumentException("Cannot work with anonymous classes")
    return ObjectKey(className, descriptor)
}

fun objectKey(
    clazz: Class<*>,
    descriptor: Descriptor<*>?
): ObjectKey {
    val className = clazz.canonicalName
        ?: throw IllegalArgumentException("Cannot work with anonymous classes")
    return ObjectKey(className, descriptor)
}