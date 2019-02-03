package net.apptronic.common.core.component.di

import kotlin.reflect.KClass

class Parameters {

    private val objects = mutableMapOf<ObjectKey, Any>()

    internal fun <ObjectType : Any> add(
        clazz: KClass<ObjectType>,
        name: String = "",
        instance: ObjectType
    ) {
        objects[ObjectKey(clazz, name)] = instance
    }

    internal fun <ObjectType : Any> get(
        clazz: KClass<ObjectType>, name: String
    ): ObjectType? {
        return objects[ObjectKey(clazz, name)] as? ObjectType
    }

}

class Builder internal constructor(private val params: Parameters) {

    fun <ObjectType : Any> add(
        clazz: KClass<ObjectType>,
        name: String = "",
        instance: ObjectType
    ) {
        params.add(clazz, name, instance)
    }

    inline fun <reified ObjectType : Any> add(
        name: String = "",
        instance: ObjectType
    ) {
        add(ObjectType::class, name, instance)
    }

}

fun parameters(builder: Builder.() -> Unit): Parameters {
    val result = Parameters()
    builder.invoke(Builder(result))
    return result
}