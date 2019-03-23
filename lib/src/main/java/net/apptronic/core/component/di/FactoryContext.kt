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
        name: String = ""
    ): ObjectType {
        return inject(ObjectType::class, name)
    }

    inline fun <reified ObjectType : Any> injectLazy(
        name: String = ""
    ): Lazy<ObjectType> {
        return injectLazy(ObjectType::class, name)
    }

    inline fun <reified ObjectType : Any> injectLazy(
        clazz: KClass<ObjectType>,
        name: String = ""
    ): Lazy<ObjectType> {
        return lazy {
            inject(clazz, name)
        }
    }

    fun <ObjectType : Any> inject(
        clazz: KClass<ObjectType>,
        name: String = ""
    ): ObjectType {
        return parameters.get(objectKey(clazz, name)) ?: context.get(clazz, name)
    }

    internal fun <ObjectType> inject(
        objectKey: ObjectKey
    ): ObjectType {
        return parameters.get(objectKey) ?: context.get(objectKey)
    }

}