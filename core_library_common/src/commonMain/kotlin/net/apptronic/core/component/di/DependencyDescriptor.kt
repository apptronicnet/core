package net.apptronic.core.component.di

import net.apptronic.core.base.SerialIdGenerator
import kotlin.reflect.KClass

/**
 * Defines injectable type. Descriptor object is key itself, so one descriptor should be defined as single instance
 * object across whole app code. Two descriptor objects are different keys and will be interpreted by
 * [DependencyDispatcher] as two different keys.
 */
sealed class DependencyDescriptor<T>(
        internal val whereCreated: String
) {

    private companion object {
        val idGenerator by lazy {
            SerialIdGenerator()
        }
    }

    internal val descriptorId: Int = idGenerator.nextId().toInt()

    override fun hashCode(): Int {
        return descriptorId
    }

    override fun equals(other: Any?): Boolean {
        return this === other
    }

    internal fun toObjectKey(): ObjectKey {
        return objectKey(this)
    }
}

private class KotlinClassDescriptor<T : Any>(
        private val clazz: KClass<T>,
        whereCreated: String
) : DependencyDescriptor<T>(whereCreated) {

    override fun toString(): String {
        return "Descriptor#$descriptorId/class:${clazz.qualifiedName}@$whereCreated"
    }

}

private class KotlinNullableClassDescriptor<T : Any>(
        private val clazz: KClass<T>,
        whereCreated: String
) : DependencyDescriptor<T?>(whereCreated) {

    override fun toString(): String {
        return "Descriptor#$descriptorId/class:${clazz.qualifiedName}@$whereCreated"
    }

}

/**
 * Create [DependencyDescriptor] of type [T].
 */
fun <T : Any> dependencyDescriptor(clazz: KClass<T>, name: String = ""): DependencyDescriptor<T> {
    return KotlinClassDescriptor(clazz, name)
}


/**
 * Create nullable [DependencyDescriptor] of type [T]. This means that provided/injected object may be null.
 */
fun <T : Any> dependencyDescriptorNullable(
        clazz: KClass<T>,
        name: String = ""
): DependencyDescriptor<T?> {
    return KotlinNullableClassDescriptor(clazz, name)
}

/**
 * Create [DependencyDescriptor] of type [T].
 */
inline fun <reified T : Any> dependencyDescriptor(name: String = ""): DependencyDescriptor<T> {
    return dependencyDescriptor(T::class, name)
}

/**
 * Create nullable [DependencyDescriptor] of type [T]. This means that provided/injected object may be null.
 */
inline fun <reified T : Any> dependencyDescriptorNullable(name: String = ""): DependencyDescriptor<T?> {
    return dependencyDescriptorNullable(T::class, name)
}