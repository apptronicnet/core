package net.apptronic.common.core.component.di

import kotlin.reflect.KClass

/**
 * Unique key for object in DI context
 */
data class ObjectKey internal constructor(
    val className: String,
    val name: String = ""
)

fun objectKey(
    className: String,
    name: String = ""
): ObjectKey {
    return ObjectKey(className, name)
}

fun objectKey(
    clazz: KClass<*>,
    name: String = ""
): ObjectKey {
    val className = clazz.qualifiedName
        ?: throw IllegalArgumentException("Cannot work with anonymous classes")
    return ObjectKey(className, name)
}

fun objectKey(
    clazz: Class<*>,
    name: String = ""
): ObjectKey {
    val className = clazz.canonicalName
        ?: throw IllegalArgumentException("Cannot work with anonymous classes")
    return ObjectKey(className, name)
}