package net.apptronic.core.component.di

import kotlin.reflect.KClass

class Parameters {

    private val instances = mutableMapOf<ObjectKey, Any>()

    internal fun <ObjectType : Any> add(
        clazz: KClass<ObjectType>,
        descriptor: Descriptor<ObjectType>? = null,
        instance: ObjectType
    ) {
        instances[objectKey(clazz, descriptor)] = instance
    }

    internal fun <ObjectType> get(objectKey: ObjectKey): ObjectType? {
        return instances[objectKey] as? ObjectType
    }

}

class Builder internal constructor(private val params: Parameters) {

    fun <ObjectType : Any> add(
        clazz: KClass<ObjectType>,
        descriptor: Descriptor<ObjectType>? = null,
        instance: ObjectType
    ) {
        params.add(clazz, descriptor, instance)
    }

    inline fun <reified ObjectType : Any> add(
        descriptor: Descriptor<ObjectType>? = null,
        instance: ObjectType
    ) {
        add(ObjectType::class, descriptor, instance)
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