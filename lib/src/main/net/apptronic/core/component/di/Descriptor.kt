package net.apptronic.core.component.di

import net.apptronic.core.base.AtomicEntity
import kotlin.reflect.KClass

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

fun <T : Any> createDescriptor(clazz: KClass<T>, whereCreated: String? = null): Descriptor<T> {
    val resultWhereCreated = whereCreated ?: Exception().stackTrace[1].toString()
    return KotlinClassDescriptor(clazz, resultWhereCreated)
}

fun <T : Any> createNullableDescriptor(
    clazz: KClass<T>,
    whereCreated: String? = null
): Descriptor<T?> {
    val resultWhereCreated = whereCreated ?: Exception().stackTrace[1].toString()
    return KotlinNullableClassDescriptor(clazz, resultWhereCreated)
}

inline fun <reified T : Any> createDescriptor(): Descriptor<T> {
    val whereCreated = Exception().stackTrace[2].toString()
    return createDescriptor(T::class, whereCreated)
}

inline fun <reified T : Any> createNullableDescriptor(): Descriptor<T?> {
    val whereCreated = Exception().stackTrace[2].toString()
    return createNullableDescriptor(T::class, whereCreated)
}

fun <T> classDescriptor(clazz: Class<T>): Descriptor<T> {
    val whereCreated = Exception().stackTrace[1].toString()
    return NamedDescriptor("Class:" + clazz.canonicalName, whereCreated)
}