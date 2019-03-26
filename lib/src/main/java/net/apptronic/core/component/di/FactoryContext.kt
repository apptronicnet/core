package net.apptronic.core.component.di

import net.apptronic.core.component.lifecycle.LifecycleStage
import kotlin.reflect.KClass

/**
 * Context of creating methods in module definition
 */
class FactoryContext(
    private val context: DependencyProvider,
    private val parameters: Parameters,
    internal val localLifecycleStage: LifecycleStage,
    internal val callerLifecycleStage: LifecycleStage
) {

    inline fun <reified ObjectType : Any> inject(
        descriptor: Descriptor<ObjectType>? = null
    ): ObjectType {
        return inject(ObjectType::class, descriptor)
    }

    inline fun <reified ObjectType : Any> injectLazy(
        descriptor: Descriptor<ObjectType>? = null
    ): Lazy<ObjectType> {
        return injectLazy(ObjectType::class, descriptor)
    }

    inline fun <reified ObjectType : Any> injectLazy(
        clazz: KClass<ObjectType>,
        descriptor: Descriptor<ObjectType>? = null
    ): Lazy<ObjectType> {
        return lazy {
            inject(clazz, descriptor)
        }
    }

    fun <ObjectType : Any> inject(
        clazz: KClass<ObjectType>,
        descriptor: Descriptor<ObjectType>? = null
    ): ObjectType {
        return parameters.get(objectKey(clazz, descriptor)) ?: context.get(clazz, descriptor)
    }

    internal fun <ObjectType> inject(
        objectKey: ObjectKey
    ): ObjectType {
        return parameters.get(objectKey) ?: context.get(objectKey)
    }

}