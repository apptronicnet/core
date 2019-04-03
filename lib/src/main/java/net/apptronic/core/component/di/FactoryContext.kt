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

    inline fun <reified ObjectType : Any> inject(): ObjectType {
        return inject(ObjectType::class)
    }

    inline fun <reified ObjectType : Any> injectLazy(): Lazy<ObjectType> {
        return injectLazy(ObjectType::class)
    }

    fun <ObjectType : Any> inject(
        clazz: KClass<ObjectType>
    ): ObjectType {
        return inject(objectKey(clazz))
    }

    fun <ObjectType : Any> injectLazy(
        clazz: KClass<ObjectType>
    ): Lazy<ObjectType> {
        return injectLazy(objectKey(clazz))
    }

    fun <ObjectType : Any> inject(
        descriptor: Descriptor<ObjectType>
    ): ObjectType {
        return inject(objectKey(descriptor))
    }

    fun <ObjectType : Any> injectLazy(
        descriptor: Descriptor<ObjectType>
    ): Lazy<ObjectType> {
        return injectLazy(objectKey(descriptor))
    }

    internal fun <ObjectType> inject(
        objectKey: ObjectKey
    ): ObjectType {
        return parameters.get(objectKey) ?: context.inject(objectKey)
    }

    internal fun <ObjectType> injectLazy(
        objectKey: ObjectKey
    ): Lazy<ObjectType> {
        return parameters.get(objectKey) ?: context.injectLazy(objectKey)
    }

}