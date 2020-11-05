package net.apptronic.core.context.di

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
            descriptor: DependencyDescriptor<ObjectType>,
            instance: ObjectType
    ) {
        instances[descriptor.toObjectKey()] = instance
    }

    @Suppress("UNCHECKED_CAST")
    internal fun <ObjectType> get(objectKey: ObjectKey): ObjectType? {
        return instances[objectKey] as? ObjectType
    }

    internal fun getInstanceNames(): List<String> {
        return instances.entries.map {
            "${it.key}=${it.value}}"
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other is Parameters) {
            val left = instances.entries.sortedBy { it.key }
            val right = other.instances.entries.sortedBy { it.key }
            if (left.size == right.size) {
                for (index in left.indices) {
                    val p1 = left[index]
                    val p2 = right[index]
                    if (p1.key != p2.key || p1.value != p2.value) {
                        return false
                    }
                }
                return true
            }
        }
        return false
    }

    override fun hashCode(): Int {
        return instances.entries.sumBy {
            it.key.hashCode() + it.value.hashCode()
        }
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
            descriptor: DependencyDescriptor<ObjectType>,
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