package net.apptronic.core.component.di

import net.apptronic.core.base.AtomicEntity
import kotlin.reflect.KClass

abstract class Descriptor<T> {

    private companion object {
        val id = AtomicEntity<Int>(0)
    }

    private val descriptorId: Int = id.let {
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

    abstract fun toObjectKey(): ObjectKey

}

private class KotlinClassDescriptor<T : Any>(
    private val clazz: KClass<T>
) : Descriptor<T>() {

    override fun toObjectKey(): ObjectKey {
        return objectKey(clazz)
    }

}

private class NamedDescriptor<T>(
    private val name: String
) : Descriptor<T>() {

    override fun toObjectKey(): ObjectKey {
        return objectKey(name)
    }

}


fun <T : Any> createDescriptor(clazz: KClass<T>): Descriptor<T> {
    return KotlinClassDescriptor(clazz)
}

inline fun <reified T : Any> createDescriptor(): Descriptor<T> {
    return createDescriptor(T::class)
}

fun <T> classDescriptor(clazz: Class<T>): Descriptor<T> {
    return NamedDescriptor("platform:" + clazz.canonicalName)
}