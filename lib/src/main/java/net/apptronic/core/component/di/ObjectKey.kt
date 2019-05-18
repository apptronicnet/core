package net.apptronic.core.component.di

import kotlin.reflect.KClass

/**
 * Unique key for object in DI context
 */
data class ObjectKey internal constructor(
    val className: String,
    val descriptor: Descriptor<*>?
) {

    override fun toString(): String {
        return if (descriptor != null) {
            descriptor.toString()
        } else {
            "Class=$className"
        }
    }

}

fun objectKey(
    name: String
): ObjectKey {
    return ObjectKey(name, null)
}

fun objectKey(
    clazz: Class<*>
): ObjectKey {
    val className = clazz.canonicalName
        ?: throw IllegalArgumentException("Cannot work with anonymous classes")
    return ObjectKey(className, null)
}

fun objectKey(
    clazz: KClass<*>
): ObjectKey {
    val className = clazz.qualifiedName
        ?: throw IllegalArgumentException("Cannot work with anonymous classes")
    return ObjectKey(className, null)
}

fun objectKey(
    descriptor: Descriptor<*>
): ObjectKey {
    return ObjectKey("", descriptor)
}