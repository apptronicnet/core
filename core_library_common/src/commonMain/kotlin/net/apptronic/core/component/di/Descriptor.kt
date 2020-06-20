package net.apptronic.core.component.di

import net.apptronic.core.base.concurrent.AtomicEntity
import kotlin.reflect.KClass

/**
 * Defines injectable type. Descriptor object is key itself, so one descriptor should be defined as single instance
 * object across whole app code. Two descriptor objects are different keys and will be interpreted by
 * [DependencyDispatcher] as two different keys.
 */
sealed class Descriptor<T>(
        internal val whereCreated: String
) {

    private companion object {
        val id = AtomicEntity<Int>(0)
    }

    internal val descriptorId: Int = id.let {
        val value = it.get()
        it.set(value + 1)
        value
    }

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
) : Descriptor<T>(whereCreated) {

    override fun toString(): String {
        return "Descriptor#$descriptorId/class:${clazz.qualifiedName}@$whereCreated"
    }

}

private class KotlinNullableClassDescriptor<T : Any>(
    private val clazz: KClass<T>,
    whereCreated: String
) : Descriptor<T?>(whereCreated) {

    override fun toString(): String {
        return "Descriptor#$descriptorId/class:${clazz.qualifiedName}@$whereCreated"
    }

}

/**
 * Create [Descriptor] of type [T].
 */
fun <T : Any> createDescriptor(clazz: KClass<T>, name: String = ""): Descriptor<T> {
    return KotlinClassDescriptor(clazz, name)
}


/**
 * Create nullable [Descriptor] of type [T]. This means that provided/injected object may be null.
 */
fun <T : Any> createNullableDescriptor(
        clazz: KClass<T>,
        name: String = ""
): Descriptor<T?> {
    return KotlinNullableClassDescriptor(clazz, name)
}

/**
 * Create [Descriptor] of type [T].
 */
inline fun <reified T : Any> createDescriptor(name: String = ""): Descriptor<T> {
    return createDescriptor(T::class, name)
}

/**
 * Create nullable [Descriptor] of type [T]. This means that provided/injected object may be null.
 */
inline fun <reified T : Any> createNullableDescriptor(name: String = ""): Descriptor<T?> {
    return createNullableDescriptor(T::class, name)
}