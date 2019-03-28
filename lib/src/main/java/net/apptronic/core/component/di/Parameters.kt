package net.apptronic.core.component.di

import kotlin.reflect.KClass

class Parameters {

    private val instances = mutableMapOf<ObjectKey, Any>()

    internal fun <ObjectType : Any> add(
        clazz: KClass<ObjectType>,
        instance: ObjectType
    ) {
        instances[objectKey(clazz)] = instance
    }

    internal fun <ObjectType : Any> add(
        descriptor: Descriptor<ObjectType>,
        instance: ObjectType
    ) {
        instances[objectKey(descriptor)] = instance
    }

    internal fun <ObjectType> get(objectKey: ObjectKey): ObjectType? {
        return instances[objectKey] as? ObjectType
    }

}

class Builder internal constructor(private val params: Parameters) {

    inline fun <reified ObjectType : Any> add(
        instance: ObjectType
    ) {
        add(ObjectType::class, instance)
    }

    fun <ObjectType : Any> add(
        clazz: KClass<ObjectType>,
        instance: ObjectType
    ) {
        params.add(clazz, instance)
    }

    fun <ObjectType : Any> add(
        descriptor: Descriptor<ObjectType>,
        instance: ObjectType
    ) {
        params.add(descriptor, instance)
    }

}

fun parameters(builder: Builder.() -> Unit): Parameters {
    val result = Parameters()
    builder.invoke(Builder(result))
    return result
}

fun emptyParameters() = parameters {
    // empty
}