package net.apptronic.core.component.di

import kotlin.reflect.KClass

sealed class Descriptor<T>(
    internal val whereCreated: String
) {

    private companion object {
        var id: Int = 0
    }

    internal val descriptorId: Int = id++

    override fun hashCode(): Int {
        return descriptorId
    }

    override fun equals(other: Any?): Boolean {
        return this === other
    }

    fun toObjectKey(): ObjectKey {
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

private class NamedDescriptor<T>(
    private val name: String,
    whereCreated: String
) : Descriptor<T>(whereCreated) {

    override fun toString(): String {
        return "Descriptor#$descriptorId/name:$name@$whereCreated"
    }

}

fun <T : Any> createDescriptor(clazz: KClass<T>, name: String = ""): Descriptor<T> {
    return KotlinClassDescriptor(clazz, name)
}

fun <T : Any> createNullableDescriptor(
        clazz: KClass<T>,
        name: String = ""
): Descriptor<T?> {
    return KotlinNullableClassDescriptor(clazz, name)
}

inline fun <reified T : Any> createDescriptor(name: String = ""): Descriptor<T> {
    return createDescriptor(T::class, name)
}

inline fun <reified T : Any> createNullableDescriptor(name: String = ""): Descriptor<T?> {
    return createNullableDescriptor(T::class, name)
}